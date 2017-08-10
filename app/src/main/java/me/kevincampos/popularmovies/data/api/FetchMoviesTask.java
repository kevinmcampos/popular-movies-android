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
import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;

public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FetchMoviesTask";

    public interface FetchMoviesCallback {
        void onSuccess(List<Movie> movies);
        void onFailed(String errorText);
    }

    private Context context;
    private String sortOrder;
    private int pageIndex;
    private FetchMoviesCallback callback;
    private List<Movie> movies;
    private String error;

    public FetchMoviesTask(Context context, String sortOrder, int pageIndex, FetchMoviesCallback callback) {
        this.context = context;
        this.sortOrder = sortOrder;
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
        try {
            HTTPResponse httpResponse = new HTTPClient().execute(context, "GET", mountUrl().toString());
            JSONObject responseAsJSON = httpResponse.getResponseAsJSON();

            JSONArray moviesJSON = responseAsJSON.getJSONArray("results");
            for (int i = 0; i < moviesJSON.length(); i++) {
                JSONObject movieJSON = moviesJSON.getJSONObject(i);
                Movie movie = Movie.fromJSON(movieJSON);
                movies.add(movie);
            }

        } catch (HTTPClient.NotConnectedException e) {
            error = context.getString(R.string.no_internet_connection);
        } catch (HTTPClient.InternalErrorException | JSONException e) {
            error = context.getString(R.string.internal_error);
            e.printStackTrace();
        }
    }

    private Uri mountUrl() {
        final Uri.Builder FETCH_MOVIES_URL = new Uri.Builder()
                .scheme("http")
                .path("api.themoviedb.org")
                .appendPath("3");

        if (sortOrder.equals(context.getString(R.string.pref_order_most_popular_value))) {
            FETCH_MOVIES_URL
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", "popularity.desc");
        } else if (sortOrder.equals(context.getString(R.string.pref_order_top_rated_value))) {
            FETCH_MOVIES_URL
                    .appendPath("movie")
                    .appendPath("top_rated");
        } else {
            throw new RuntimeException("Could not mount fetch movies URL, sort order is unknown: " + sortOrder);
        }

        FETCH_MOVIES_URL.appendQueryParameter("page", String.valueOf(pageIndex))
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);

        return FETCH_MOVIES_URL.build();
    }

    @Override
    protected void onPostExecute(Void o) {
        if (error == null) {
            callback.onSuccess(movies);
        } else {
            callback.onFailed(error);
        }
    }
}
