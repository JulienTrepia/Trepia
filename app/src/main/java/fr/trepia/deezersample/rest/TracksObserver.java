package fr.trepia.deezersample.rest;

import fr.trepia.deezersample.model.Tracks;
import io.reactivex.Observable;

public interface TracksObserver {

    Observable<Tracks> getPlaylistTracks(String playlistId);
    Observable<Tracks> getPlaylistTracksFromIndex(String url);
}
