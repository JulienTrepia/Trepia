package fr.trepia.deezersample.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.app.App;
import fr.trepia.deezersample.model.Playlists;
import fr.trepia.deezersample.utils.ExtrasUtils;
import fr.trepia.deezersample.view.TracksActivity;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlists.PlaylistData> playlists;

    public PlaylistAdapter(List<Playlists.PlaylistData> playlists) {
        this.playlists = playlists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_playlists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Playlists.PlaylistData playlist = playlists.get(position);

        if (playlist != null) {
            Uri imgUri = Uri.parse("res:///" + R.drawable.default_cover);
            if (playlist.getPicture_medium() != null)
                imgUri = Uri.parse(playlist.getPicture_medium());

            holder.txtViewTitle.setText(playlist.getTitle());
            holder.sdView.setImageURI(imgUri);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startPlaylistActivity(playlist);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size() : 0;
    }

    private void startPlaylistActivity(Playlists.PlaylistData playlist) {
        Intent intent = new Intent(App.getContext(), TracksActivity.class);
        intent.putExtra(ExtrasUtils.EXTRA_PLAYLIST_ID, playlist.getId());
        intent.putExtra(ExtrasUtils.EXTRA_PLAYLIST_TITLE, playlist.getTitle());
        intent.putExtra(ExtrasUtils.EXTRA_PLAYLIST_AUTHOR, playlist.getCreator().getName());
        intent.putExtra(ExtrasUtils.EXTRA_PLAYLIST_COVER, playlist.getPicture_big());
        intent.putExtra(ExtrasUtils.EXTRA_PLAYLIST_DURATION, playlist.getDuration());
        App.getContext().startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtViewTitle;
        private SimpleDraweeView sdView;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtViewTitle = (TextView) view.findViewById(R.id.txtViewTitle);
            sdView = (SimpleDraweeView) view.findViewById(R.id.sdView);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }
}
