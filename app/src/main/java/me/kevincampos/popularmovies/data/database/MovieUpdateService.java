package me.kevincampos.popularmovies.data.database;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import me.kevincampos.popularmovies.data.Movie;

public class MovieUpdateService extends IntentService {

    private static final String TAG = "MovieUpdateService";

    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";

    public static void favoriteMovie(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, movie.getContentValues());
        context.startService(intent);
    }

    public static void unfavoriteMovie(Context context, Uri uri) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    public MovieUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getAction()) {
            case ACTION_INSERT: {
                ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
                performInsert(values);
                break;
            }
            case ACTION_DELETE: {
                performDelete(intent.getData());
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid action for MovieUpdateService");
        }
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(MovieContract.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new movie");
        } else {
            Log.w(TAG, "Error inserting new movie");
        }
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Deleted " + count + " movie(s)");
    }
}
