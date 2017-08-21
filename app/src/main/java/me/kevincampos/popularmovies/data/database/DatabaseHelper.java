package me.kevincampos.popularmovies.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.kevincampos.popularmovies.data.database.MovieContract.MovieColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLES = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s INTEGER, " +
                    " %s TEXT, " +
                    " %s REAL, " +
                    " %s REAL, " +
                    " %s TEXT, " +
                    " %s INTEGER, " +
                    " %s INTEGER, " +
                    " %s INTEGER, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s INTEGER)",
            MovieContract.TABLE_MOVIE,
            MovieColumns._ID,
            MovieColumns.ID,
            MovieColumns.TITLE,
            MovieColumns.POPULARITY,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.OVERVIEW,
            MovieColumns.RELEASE_YEAR,
            MovieColumns.RELEASE_MONTH,
            MovieColumns.RELEASE_DAY,
            MovieColumns.POSTER_PATH,
            MovieColumns.BACKDROP_PATH,
            MovieColumns.GENRES,
            MovieColumns.IS_ADULT
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_MOVIE);
        onCreate(db);
    }
}
