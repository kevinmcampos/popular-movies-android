package me.kevincampos.popularmovies.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.kevincampos.popularmovies.data.database.MovieContract.MovieColumns;

public class MovieProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private DatabaseHelper dbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://me.kevincampos.popularmovies/movie
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.TABLE_MOVIE,
                MOVIES);

        // content://me.kevincampos.popularmovies/movie/id
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.TABLE_MOVIE + "/#",
                MOVIE_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null; /* not used */
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                selection = (selection == null) ? "1" : selection;
                break;
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", MovieColumns.ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal query URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor queryCursor = db.query(MovieContract.TABLE_MOVIE, projection, selection, selectionArgs,
                null, null, sortOrder);

        queryCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return queryCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri insertUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long _id = db.insert(MovieContract.TABLE_MOVIE, null, values);
                if (_id > 0) {
                    insertUri = MovieContract.buildMovieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

                break;
            default:
                throw new IllegalArgumentException("Illegal insert URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Update is not implemented, URI: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", MovieColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(MovieContract.TABLE_MOVIE, selection, selectionArgs);

        if (count > 0 && getContext() != null) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

}
