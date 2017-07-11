package fr.trepia.deezersample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.adapter.PlaylistAdapter;
import fr.trepia.deezersample.model.Playlists;
import fr.trepia.deezersample.rest.PlaylistsRequester;
import fr.trepia.deezersample.viewmodel.PlaylistsViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PlaylistsActivity extends AppCompatActivity {

    private boolean isLoading = false;

    private PlaylistAdapter playlistAdapter;

    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView txtViewErrorPlaylists;

    private Observable<Playlists> eventPlaylists;
    private Observable<Playlists> eventNextPlaylists;

    private PlaylistsViewModel playlistsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);
        findViews();

        playlistsViewModel = new PlaylistsViewModel(new PlaylistsRequester());
        requestPlaylists();
    }

    @Override
    protected void onDestroy() {
        if (eventPlaylists != null)
            eventPlaylists.unsubscribeOn(Schedulers.io());

        if (eventNextPlaylists != null)
            eventNextPlaylists.unsubscribeOn(Schedulers.io());

        super.onDestroy();
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtViewErrorPlaylists = (TextView) findViewById(R.id.txtViewErrorPlaylists);
    }

    private void showPlaylists(boolean isVisible) {
        if (isVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            txtViewErrorPlaylists.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            txtViewErrorPlaylists.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(List<Playlists.PlaylistData> playlists) {
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        playlistAdapter = new PlaylistAdapter(playlists);
        recyclerView.setAdapter(playlistAdapter);
    }

    private void setOnScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLoading)
                    return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    isLoading = true;
                    requestNextPlaylists();
                }
            }
        });
    }

    private void requestPlaylists() {
        isLoading = true;

        eventPlaylists = playlistsViewModel.getUserPlaylists();
        eventPlaylists.subscribe(new Observer<Playlists>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Playlists playlists) {
                        playlistsViewModel.setPlaylists(playlists);

                        List<Playlists.PlaylistData> dataList = playlists.getPlaylists();
                        if (dataList != null && dataList.size() > 0) {
                            showPlaylists(true);
                            setAdapter(dataList);
                            setOnScrollListener();
                        }
                        else
                            showPlaylists(false);

                        isLoading = false;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showPlaylists(false);
                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void requestNextPlaylists() {
        if (playlistsViewModel.getPlaylists().getNext() != null) {
            isLoading = true;

            eventNextPlaylists = playlistsViewModel.getUserPlaylistsFromIndex();
            eventNextPlaylists.subscribe(new Observer<Playlists>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@NonNull Playlists playlists) {
                            playlistsViewModel.addNewPlaylists(playlists);

                            List<Playlists.PlaylistData> dataList = playlistsViewModel.getPlaylists().getPlaylists();
                            if (dataList != null && dataList.size() > 0) {
                                showPlaylists(true);
                                playlistAdapter.notifyDataSetChanged();
                            }
                            else
                                showPlaylists(false);

                            isLoading = false;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            isLoading = false;
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        else
            isLoading = false;
    }
}
