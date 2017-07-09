package me.kevincampos.popularmovies.data.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.BuildConfig;
import me.kevincampos.popularmovies.data.Movie;

public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FetchMoviesTask";

    public interface FetchMoviesCallback {
        void onSuccess(List<Movie> movies);
        void onFailed();
    }

    private Context context;
    private int pageIndex;
    private FetchMoviesCallback callback;
    private List<Movie> movies;

    public FetchMoviesTask(Context context, int pageIndex, FetchMoviesCallback callback) {
        this.context = context;
        this.pageIndex = pageIndex;
        this.callback = callback;
        this.movies = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void[] params) {
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
                .appendQueryParameter("page", String.valueOf(pageIndex))
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        try {
            HTTPResponse httpResponse = new HTTPClient().execute(context, "GET", FETCH_MOVIES_URL.toString());
            JSONObject responseAsJSON = httpResponse.getResponseAsJSON();

            JSONArray moviesJSON = responseAsJSON.getJSONArray("results");
            for (int i = 0; i < moviesJSON.length(); i++) {
                JSONObject movieJSON = moviesJSON.getJSONObject(i);
                Movie movie = Movie.fromJSON(movieJSON);
                movies.add(movie);
            }

        } catch (HTTPClient.NotConnectedException | HTTPClient.InternalErrorException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void o) {
        callback.onSuccess(movies);
    }
}
