package me.kevincampos.popularmovies.data.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String TABLE_MOVIE = "movie";

    public static final class MovieColumns implements BaseColumns {

        public static final String ID = "id";

        public static final String TITLE = "title";

        public static final String POPULARITY = "popularity";

        public static final String VOTE_AVERAGE = "vote_average";

        public static final String OVERVIEW = "overview";

        public static final String RELEASE_YEAR = "release_year";

        public static final String RELEASE_MONTH = "release_month";

        public static final String RELEASE_DAY = "release_day";

        public static final String POSTER_PATH = "poster_path";

        public static final String BACKDROP_PATH = "backdrop_path";

        public static final String GENRES = "genres";

        public static final String IS_ADULT = "is_adult";
    }

    public static final String CONTENT_AUTHORITY = "me.kevincampos.popularmovies";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static Uri buildMovieUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
