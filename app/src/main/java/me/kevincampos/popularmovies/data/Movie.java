package me.kevincampos.popularmovies.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movie implements Parcelable {

    private static final String TAG = "Movie";

    private final Long ID;
    public final String TITLE;
    private final Double POPULARITY;
    private final Double VOTE_AVERAGE;
    private final String OVERVIEW;
    private final Date RELEASE_DATE;
    private final String POSTER_PATH;
    private final String BACKDROP_PATH;
    private final List<Long> GENRES;
    private final Boolean IS_ADULT;

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

    public Movie(Parcel parcel) {
        ID = parcel.readLong();
        TITLE = parcel.readString();
        POPULARITY = parcel.readDouble();
        VOTE_AVERAGE = parcel.readDouble();
        OVERVIEW = parcel.readString();
        RELEASE_DATE = new Date(parcel.readLong());
        POSTER_PATH = parcel.readString();
        BACKDROP_PATH = parcel.readString();
        GENRES = parcel.readArrayList(null);
        IS_ADULT = parcel.readInt() == 1;
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

    public Uri getPosterURL() {
        return new Uri.Builder()
                .scheme("http")
                .path("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(POSTER_PATH.substring(1)) // Remove backslash from path
                .build();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(TITLE);
        dest.writeDouble(POPULARITY);
        dest.writeDouble(VOTE_AVERAGE);
        dest.writeString(OVERVIEW);
        dest.writeLong(RELEASE_DATE.getTime());
        dest.writeString(POSTER_PATH);
        dest.writeString(BACKDROP_PATH);
        dest.writeList(GENRES);
        dest.writeInt(IS_ADULT ? 1 : 0);
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}