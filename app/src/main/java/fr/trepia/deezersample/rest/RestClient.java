package fr.trepia.deezersample.rest;

import java.util.concurrent.TimeUnit;

import fr.trepia.deezersample.R;
import fr.trepia.deezersample.app.App;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static RestClient instance;
    private Retrofit retrofit;

    private RestClient() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient client = builder
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(App.getContext().getResources().getString(R.string.deezer_url_api))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static synchronized DeezerService getService() {

        if (instance == null) {
            instance = new RestClient();
        }

        return instance.retrofit.create(DeezerService.class);
    }
}
