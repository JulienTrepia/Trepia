package fr.trepia.deezersample.rest;

import fr.trepia.deezersample.model.Tracks;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class TracksRequester implements TracksObserver {

    @Override
    public Observable<Tracks> getPlaylistTracks(String userId) {
        return RestClient.getService().getPlaylistTracks(userId).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Tracks> getPlaylistTracksFromIndex(String url) {
        return RestClient.getService().getPlaylistTracksFromIndex(url).subscribeOn(Schedulers.io());
    }
}
