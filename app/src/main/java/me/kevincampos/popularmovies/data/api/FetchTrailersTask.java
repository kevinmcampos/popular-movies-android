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
import me.kevincampos.popularmovies.data.YoutubeVideo;

public class FetchTrailersTask extends AsyncTask<Void, Void, Void> {


    public interface FetchTrailersCallback {
        void onSuccess(List<YoutubeVideo> trailers);
        void onFailed(String errorText);
    }

    private final Context context;
    private final Long movieId;
    private final List<YoutubeVideo> trailers;
    private final FetchTrailersCallback callback;

    private String error;

    public FetchTrailersTask(Context context, Long movieId, FetchTrailersCallback callback) {
        this.context = context;
        this.movieId = movieId;
        this.callback = callback;
        this.trailers = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchTrailers();
        return null;
    }

    private void fetchTrailers() {
        try {
            HTTPResponse httpResponse = new HTTPClient().execute(context, "GET", mountUrl().toString());
            JSONObject responseAsJSON = httpResponse.getResponseAsJSON();

            JSONArray trailersJSON = responseAsJSON.getJSONArray("results");
            for (int i = 0; i < trailersJSON.length(); i++) {
                JSONObject trailerJSON = trailersJSON.getJSONObject(i);
                YoutubeVideo trailer = YoutubeVideo.fromJSON(trailerJSON);
                trailers.add(trailer);
            }

        } catch (HTTPClient.NotConnectedException e) {
            error = context.getString(R.string.no_internet_connection);
        } catch (HTTPClient.InternalErrorException | JSONException e) {
            error = context.getString(R.string.internal_error);
            e.printStackTrace();
        }
    }

    private Uri mountUrl() {
        return new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(movieId))
                .appendPath("videos")
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
    }

    @Override
    protected void onPostExecute(Void o) {
        if (error == null) {
            callback.onSuccess(trailers);
        } else {
            callback.onFailed(error);
        }
    }
}
