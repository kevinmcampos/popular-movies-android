package me.kevincampos.popularmovies.data.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.kevincampos.popularmovies.BuildConfig;
import me.kevincampos.popularmovies.data.Movie;

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

            JSONArray moviesJSON = responseAsJSON.getJSONArray("results");
            for (int i = 0; i < moviesJSON.length(); i++) {
                JSONObject movieJSON = moviesJSON.getJSONObject(i);
                Movie movie = Movie.fromJSON(movieJSON);
                Log.e(TAG, "Found " + movie.toString());
            }

        } catch (HTTPClient.NotConnectedException | HTTPClient.InternalErrorException | JSONException e) {
            e.printStackTrace();
        }
    }
}
