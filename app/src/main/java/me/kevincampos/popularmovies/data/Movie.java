package me.kevincampos.popularmovies.data;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Movie {

    private static final String TAG = "Movie";

    final Long ID;
    final String TITLE;
    final Double POPULARITY;
    final Double VOTE_AVERAGE;
    final String OVERVIEW;
    final Date RELEASE_DATE;
    final String POSTER_PATH;
    final String BACKDROP_PATH;
    final ArrayList<Long> GENRES;
    final Boolean IS_ADULT;

    public Movie(JSONObject movieJSON) throws JSONException {
        ID = movieJSON.getLong("id");
        TITLE = movieJSON.getString("title");
        POPULARITY = movieJSON.getDouble("popularity");
        VOTE_AVERAGE = movieJSON.getDouble("vote_average");
        final String unformattedDate = movieJSON.getString("release_date");

        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date releaseDate = null;
        try {
            releaseDate = DATE_FORMAT.parse(unformattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RELEASE_DATE = releaseDate;

        POSTER_PATH = movieJSON.getString("poster_path");
        BACKDROP_PATH = movieJSON.getString("backdrop_path");

        GENRES = new ArrayList<>();
        JSONArray genreIds = movieJSON.getJSONArray("genre_ids");
        for (int i = 0; i < genreIds.length(); i++) {
            GENRES.add(genreIds.getLong(i));
        }

        OVERVIEW = movieJSON.getString("overview");
        IS_ADULT = movieJSON.getBoolean("adult");
    }

    @Nullable
    public static Movie fromJSON(JSONObject movieJSON) {
        try {
            return new Movie(movieJSON);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to convert movie JSON into a movie.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a brief description of this movie. The exact details of the representation are
     * unspecified and subject to change, but the following may be regarded as typical:
     *
     * "[Movie #157336: title=Interstellar, popularity=37.852408, vote_average=8.1]"
     */
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "[Movie #%d: title=%s, popularity=%f.6, vote_average=%f.1]", ID, TITLE, POPULARITY, VOTE_AVERAGE);
    }
}
