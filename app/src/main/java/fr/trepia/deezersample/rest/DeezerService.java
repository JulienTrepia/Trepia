package fr.trepia.deezersample.rest;

import fr.trepia.deezersample.model.Playlists;
import fr.trepia.deezersample.model.Tracks;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface DeezerService {

    @GET("user/{userId}/playlists")
    Observable<Playlists> getUserPlaylists(@Path("userId") String userId);

    @GET
    Observable<Playlists> getUserPlaylistsFromIndex(@Url String url);

    @GET("playlist/{playlistId}/tracks")
    Observable<Tracks> getPlaylistTracks(@Path("playlistId") String playlistId);

    @GET
    Observable<Tracks> getPlaylistTracksFromIndex(@Url String url);
}
