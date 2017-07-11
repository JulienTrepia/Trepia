package fr.trepia.deezersample.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.adapter.TracksAdapter;
import fr.trepia.deezersample.model.Tracks;
import fr.trepia.deezersample.rest.TracksRequester;
import fr.trepia.deezersample.utils.DurationUtils;
import fr.trepia.deezersample.viewmodel.TracksViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TracksActivity extends AppCompatActivity {

    private boolean isLoading = false;

    private TracksAdapter tracksAdapter;

    private LinearLayoutManager layoutManager;
    private TextView txtViewTitle;
    private TextView txtViewCreator;
    private TextView txtViewDuration;
    private SimpleDraweeView sdViewHeader;
    private RecyclerView recyclerView;
    private TextView txtViewErrorTracks;

    private Observable<Tracks> eventTracks;
    private Observable<Tracks> eventNextTracks;

    private TracksViewModel tracksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        findViews();

        tracksViewModel = new TracksViewModel(new TracksRequester(), getIntent().getExtras());
        setHeader();
        requestTracks();
    }

    @Override
    protected void onDestroy() {
        if (eventTracks != null)
            eventTracks.unsubscribeOn(Schedulers.io());

        if (eventNextTracks != null)
            eventNextTracks.unsubscribeOn(Schedulers.io());

        super.onDestroy();
    }

    private void findViews() {
        txtViewTitle = (TextView) findViewById(R.id.txtViewTitle);
        txtViewCreator = (TextView) findViewById(R.id.txtViewCreator);
        txtViewDuration = (TextView) findViewById(R.id.txtViewDuration);
        sdViewHeader = (SimpleDraweeView) findViewById(R.id.sdViewHeader);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtViewErrorTracks = (TextView) findViewById(R.id.txtViewErrorTracks);
    }

    private void showTracks(boolean isVisible) {
        if (isVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            txtViewErrorTracks.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            txtViewErrorTracks.setVisibility(View.VISIBLE);
        }
    }

    private void setHeader() {
        txtViewTitle.setText(tracksViewModel.getTitle());
        txtViewCreator.setText("par " + tracksViewModel.getCreator());
        txtViewDuration.setText(" - " + DurationUtils.durationToString(tracksViewModel.getDuration()));

        Uri imgUri = Uri.parse("res:///" + R.drawable.default_cover);
        if (tracksViewModel.getCoverUrl() != null)
            imgUri = Uri.parse(tracksViewModel.getCoverUrl());

        sdViewHeader.setImageURI(imgUri);
    }

    private void setAdapter(List<Tracks.TracksData> tracks) {

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tracksAdapter = new TracksAdapter(tracks);
        recyclerView.setAdapter(tracksAdapter);
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
                    requestNextTracks();
                }
            }
        });
    }

    private void requestTracks() {
        isLoading = true;

        eventTracks = tracksViewModel.getPlaylistTracks();
        eventTracks.subscribe(new Observer<Tracks>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Tracks tracks) {
                tracksViewModel.setTracks(tracks);

                List<Tracks.TracksData> dataList = tracks.getTracks();
                if (dataList != null && dataList.size() > 0) {
                    showTracks(true);
                    setAdapter(dataList);
                    setOnScrollListener();
                }
                else
                    showTracks(false);

                isLoading = false;
            }

            @Override
            public void onError(@NonNull Throwable e) {
                showTracks(false);
                isLoading = false;
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void requestNextTracks() {
        Callback<Tracks> callback = new Callback<Tracks>() {
            @Override
            public void onResponse(Call<Tracks> call, Response<Tracks> response) {
            }

            @Override
            public void onFailure(Call<Tracks> call, Throwable t) {
            }
        };

        if (tracksViewModel.getTracks().getNext() != null) {
            isLoading = true;

            eventNextTracks = tracksViewModel.getPlaylistTracksFromIndex();
            eventNextTracks.subscribe(new Observer<Tracks>() {
                      @Override
                      public void onSubscribe(@NonNull Disposable d) {
                      }

                      @Override
                      public void onNext(@NonNull Tracks tracks) {
                          tracksViewModel.addNewTracks(tracks);

                          List<Tracks.TracksData> dataList = tracksViewModel.getTracks().getTracks();
                          if (dataList != null && dataList.size() > 0) {
                              showTracks(true);
                              tracksAdapter.notifyDataSetChanged();
                          }
                          else
                              showTracks(false);

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
