package fr.trepia.deezersample.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.model.Tracks;
import fr.trepia.deezersample.utils.DurationUtils;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    private List<Tracks.TracksData> tracks;

    public TracksAdapter(List<Tracks.TracksData> tracks) {
        this.tracks = tracks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_tracks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tracks.TracksData track = tracks.get(position);

        if (track != null) {
            Uri imgUri = Uri.parse("res:///" + R.drawable.default_cover);
            if (track.getArtist().getPicture_small() != null)
                imgUri = Uri.parse(track.getArtist().getPicture_small());

            holder.sdView.setImageURI(imgUri);
            holder.txtViewTitle.setText(track.getTitle());
            holder.txtViewSubtitle.setText(track.getArtist().getName() + " - " + DurationUtils.durationToString(track.getDuration()));
        }
    }

    @Override
    public int getItemCount() {
        return tracks != null ? tracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView sdView;
        private TextView txtViewTitle;
        private TextView txtViewSubtitle;

        public ViewHolder(View view) {
            super(view);

            txtViewTitle = (TextView) view.findViewById(R.id.txtViewTitle);
            txtViewSubtitle = (TextView) view.findViewById(R.id.txtViewSubtitle);
            sdView = (SimpleDraweeView) view.findViewById(R.id.sdView);
        }
    }
}
