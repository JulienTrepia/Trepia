package fr.trepia.deezersample.rest;

import fr.trepia.deezersample.model.Playlists;
import io.reactivex.Observable;

public interface PlaylistsObserver {

    Observable<Playlists> getUserPlaylists(String userId);
    Observable<Playlists> getUserPlaylistsFromIndex(String url);
}
