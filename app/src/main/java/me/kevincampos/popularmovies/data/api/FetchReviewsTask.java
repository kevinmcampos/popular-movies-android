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
import me.kevincampos.popularmovies.data.Review;

public class FetchReviewsTask extends AsyncTask<Void, Void, Void> {

    public interface FetchReviewsCallback {
        void onSuccess(List<Review> reviews);
        void onFailed(String errorText);
    }

    private final Context context;
    private final Long movieId;
    private final List<Review> reviews;
    private final FetchReviewsCallback callback;

    private String error;

    public FetchReviewsTask(Context context, Long movieId, FetchReviewsCallback callback) {
        this.context = context;
        this.movieId = movieId;
        this.callback = callback;
        this.reviews = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchReviews();
        return null;
    }

    private void fetchReviews() {
        try {
            HTTPResponse httpResponse = new HTTPClient().execute(context, "GET", mountUrl().toString());
            JSONObject responseAsJSON = httpResponse.getResponseAsJSON();

            JSONArray reviewsJSON = responseAsJSON.getJSONArray("results");
            for (int i = 0; i < reviewsJSON.length(); i++) {
                JSONObject reviewJSON = reviewsJSON.getJSONObject(i);
                Review review = Review.fromJSON(reviewJSON);
                reviews.add(review);
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
                .appendPath("reviews")
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
    }

    @Override
    protected void onPostExecute(Void o) {
        if (error == null) {
            callback.onSuccess(reviews);
        } else {
            callback.onFailed(error);
        }
    }
}
