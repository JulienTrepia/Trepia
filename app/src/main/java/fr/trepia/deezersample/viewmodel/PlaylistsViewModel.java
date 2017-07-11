package fr.trepia.deezersample.viewmodel;

import java.util.List;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.app.App;
import fr.trepia.deezersample.model.Playlists;
import fr.trepia.deezersample.rest.PlaylistsRequester;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaylistsViewModel {

    private Playlists playlists;
    private PlaylistsRequester playlistsRequester;

    public PlaylistsViewModel(PlaylistsRequester playlistsRequester) {
        this.playlistsRequester = playlistsRequester;
        playlists = new Playlists();
    }

    public Playlists getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Playlists playlists) {
        this.playlists = playlists;
    }

    public Observable<Playlists> getUserPlaylists() {
        String userId = App.getContext().getResources().getString(R.string.deezer_user_id);
        return playlistsRequester.getUserPlaylists(userId).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Playlists> getUserPlaylistsFromIndex() {
        String url = playlists.getNext().replace(App.getContext().getResources().getString(R.string.deezer_url_api), "");
        return playlistsRequester.getUserPlaylistsFromIndex(url).observeOn(AndroidSchedulers.mainThread());
    }

    public void addNewPlaylists(Playlists newPlaylists) {
        List<Playlists.PlaylistData> playlistsData = playlists.getPlaylists();

        for (Playlists.PlaylistData data : newPlaylists.getPlaylists()) {
            if (!playlistsData.contains(data))
                playlistsData.add(data);
        }

        playlists.setNext(newPlaylists.getNext());
    }
}
