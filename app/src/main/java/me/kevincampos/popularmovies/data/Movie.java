package me.kevincampos.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.kevincampos.popularmovies.data.database.MovieContract.MovieColumns;

public class Movie implements Parcelable {

    private static final String TAG = "Movie";

    public final Long ID;
    public final String TITLE;
    private final Double POPULARITY;
    public final Double VOTE_AVERAGE;
    public final String OVERVIEW;
    private final Integer RELEASE_YEAR;
    private final Integer RELEASE_MONTH;
    private final Integer RELEASE_DAY;
    private final String POSTER_PATH;
    private final String BACKDROP_PATH;
    public final String GENRES;
    private final Boolean IS_ADULT;

    public Movie(JSONObject movieJSON) throws JSONException {
        ID = movieJSON.getLong("id");
        TITLE = movieJSON.getString("title");
        POPULARITY = movieJSON.getDouble("popularity");
        VOTE_AVERAGE = movieJSON.getDouble("vote_average");
        final String unformattedDate = movieJSON.getString("release_date");

        RELEASE_YEAR = Integer.valueOf(unformattedDate.substring(0, 4));
        RELEASE_MONTH = Integer.valueOf(unformattedDate.substring(5, 7));
        RELEASE_DAY = Integer.valueOf(unformattedDate.substring(8, 10));

        POSTER_PATH = movieJSON.getString("poster_path");
        BACKDROP_PATH = movieJSON.getString("backdrop_path");

        List<Long> genresList = new ArrayList<>();
        JSONArray genreIds = movieJSON.getJSONArray("genre_ids");
        for (int i = 0; i < genreIds.length(); i++) {
            genresList.add(genreIds.getLong(i));
        }
        GENRES = formatGenres(genresList);

        OVERVIEW = movieJSON.getString("overview");
        IS_ADULT = movieJSON.getBoolean("adult");
    }

    public Movie(Parcel parcel) {
        ID = parcel.readLong();
        TITLE = parcel.readString();
        POPULARITY = parcel.readDouble();
        VOTE_AVERAGE = parcel.readDouble();
        OVERVIEW = parcel.readString();
        RELEASE_YEAR = parcel.readInt();
        RELEASE_MONTH = parcel.readInt();
        RELEASE_DAY = parcel.readInt();
        POSTER_PATH = parcel.readString();
        BACKDROP_PATH = parcel.readString();
        GENRES = parcel.readString();
        IS_ADULT = parcel.readInt() == 1;
    }

    public Movie(Cursor cursor) {
        int columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.ID);
        ID = cursor.getLong(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.TITLE);
        TITLE = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.POPULARITY);
        POPULARITY = cursor.getDouble(columnIndex);

        columnIndex = cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE);
        VOTE_AVERAGE = cursor.getDouble(columnIndex);

        columnIndex = cursor.getColumnIndex(MovieColumns.OVERVIEW);
        OVERVIEW = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.RELEASE_YEAR);
        RELEASE_YEAR = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.RELEASE_MONTH);
        RELEASE_MONTH = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.RELEASE_DAY);
        RELEASE_DAY = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.POSTER_PATH);
        POSTER_PATH = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.BACKDROP_PATH);
        BACKDROP_PATH = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.GENRES);
        GENRES = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MovieColumns.IS_ADULT);
        IS_ADULT = cursor.getInt(columnIndex) == 1;
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

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieColumns.ID, ID);
        values.put(MovieColumns.TITLE, TITLE);
        values.put(MovieColumns.POPULARITY, POPULARITY);
        values.put(MovieColumns.VOTE_AVERAGE, VOTE_AVERAGE);
        values.put(MovieColumns.OVERVIEW, OVERVIEW);
        values.put(MovieColumns.RELEASE_YEAR, RELEASE_YEAR);
        values.put(MovieColumns.RELEASE_MONTH, RELEASE_MONTH);
        values.put(MovieColumns.RELEASE_DAY, RELEASE_DAY);
        values.put(MovieColumns.POSTER_PATH, POSTER_PATH);
        values.put(MovieColumns.BACKDROP_PATH, BACKDROP_PATH);
        values.put(MovieColumns.GENRES, GENRES);
        values.put(MovieColumns.IS_ADULT, IS_ADULT);
        return values;
    }

    private String formatGenres(List<Long> genresList) {
        final StringBuilder genresFormatted = new StringBuilder();
        for (Long genreId : genresList) {
            String genreName = GenreHelper.getGenreById(genreId.intValue());
            genresFormatted.append(genreName).append(" | ");
        }
        return genresFormatted.substring(0, genresFormatted.length() - 3);
    }

    public Uri getPosterURL() {
        return new Uri.Builder()
                .scheme("http")
                .path("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w342")
                .appendPath(POSTER_PATH.substring(1)) // Remove backslash from path
                .build();
    }

    public Uri getBackdropURL() {
        return new Uri.Builder()
                .scheme("http")
                .path("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w780")
                .appendPath(BACKDROP_PATH.substring(1)) // Remove backslash from path
                .build();
    }

    public String getReleaseDateFormatted() {
        final Calendar RELEASE_DATE = Calendar.getInstance();
        RELEASE_DATE.set(RELEASE_YEAR, RELEASE_MONTH, RELEASE_DAY);
        String monthName = String.format(Locale.US, "%tB", RELEASE_DATE);

        return String.format("%s %s, %s", monthName, RELEASE_DAY, RELEASE_YEAR);
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
        dest.writeInt(RELEASE_YEAR);
        dest.writeInt(RELEASE_MONTH);
        dest.writeInt(RELEASE_DAY);
        dest.writeString(POSTER_PATH);
        dest.writeString(BACKDROP_PATH);
        dest.writeString(GENRES);
        dest.writeInt(IS_ADULT ? 1 : 0);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
