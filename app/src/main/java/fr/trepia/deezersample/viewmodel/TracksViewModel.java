package fr.trepia.deezersample.viewmodel;

import android.os.Bundle;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.app.App;
import fr.trepia.deezersample.model.Tracks;
import fr.trepia.deezersample.rest.TracksRequester;
import fr.trepia.deezersample.utils.ExtrasUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TracksViewModel {

    private long id;
    private String title;
    private String creator;
    private String coverUrl;
    private long duration;

    private Tracks tracks;
    private TracksRequester tracksRequester;

    public TracksViewModel(TracksRequester tracksRequester, Bundle extras) {
        this.tracksRequester = tracksRequester;
        getExtras(extras);
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public long getDuration() {
        return duration;
    }

    public Observable<Tracks> getPlaylistTracks() {
        if (id > 0)
            return tracksRequester.getPlaylistTracks(Long.toString(id)).observeOn(AndroidSchedulers.mainThread());
        else
            return null;
    }

    public Observable<Tracks> getPlaylistTracksFromIndex() {
        String url = tracks.getNext().replace(App.getContext().getResources().getString(R.string.deezer_url_api), "");
        return tracksRequester.getPlaylistTracksFromIndex(url).observeOn(AndroidSchedulers.mainThread());
    }

    public void addNewTracks(Tracks newTracks) {
        List<Tracks.TracksData> tracksData = tracks.getTracks();

        for (Tracks.TracksData data : newTracks.getTracks()) {
            if (!tracksData.contains(data))
                tracksData.add(data);
        }

        tracks.setNext(newTracks.getNext());
    }

    private void getExtras(Bundle extras) {
        if (extras.containsKey(ExtrasUtils.EXTRA_PLAYLIST_ID))
            id = extras.getLong(ExtrasUtils.EXTRA_PLAYLIST_ID);

        if (extras.containsKey(ExtrasUtils.EXTRA_PLAYLIST_TITLE))
            title = extras.getString(ExtrasUtils.EXTRA_PLAYLIST_TITLE);

        if (extras.containsKey(ExtrasUtils.EXTRA_PLAYLIST_AUTHOR))
            creator = extras.getString(ExtrasUtils.EXTRA_PLAYLIST_AUTHOR);

        if (extras.containsKey(ExtrasUtils.EXTRA_PLAYLIST_COVER))
            coverUrl = extras.getString(ExtrasUtils.EXTRA_PLAYLIST_COVER);

        if (extras.containsKey(ExtrasUtils.EXTRA_PLAYLIST_DURATION))
            duration = extras.getLong(ExtrasUtils.EXTRA_PLAYLIST_DURATION);
    }
}
