package me.kevincampos.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.kevincampos.popularmovies.network.HTTPClient;
import me.kevincampos.popularmovies.network.HTTPResponse;

public class FetchMoviesTask extends AsyncTask {

    private static final String TAG = "FetchMoviesTask";

    private Context context;

    public FetchMoviesTask(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        fetchMovies();
        return null;
    }

    private void fetchMovies() {
        final Uri FETCH_MOVIES_URL = new Uri.Builder()
                .scheme("http")
                .path("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        try {
            HTTPResponse httpResponse = new HTTPClient().execute(context, "GET", FETCH_MOVIES_URL.toString());
            JSONObject responseAsJSON = httpResponse.getResponseAsJSON();

            Log.e(TAG, responseAsJSON.toString(4));
        } catch (HTTPClient.NotConnectedException | HTTPClient.InternalErrorException | JSONException e) {
            e.printStackTrace();
        }
    }
}
