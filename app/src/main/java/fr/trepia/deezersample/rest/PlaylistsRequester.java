package fr.trepia.deezersample.rest;

import fr.trepia.deezersample.model.Playlists;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PlaylistsRequester implements PlaylistsObserver {

    @Override
    public Observable<Playlists> getUserPlaylists(String userId) {
        return RestClient.getService().getUserPlaylists(userId).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Playlists> getUserPlaylistsFromIndex(String url) {
        return RestClient.getService().getUserPlaylistsFromIndex(url).subscribeOn(Schedulers.io());
    }
}
