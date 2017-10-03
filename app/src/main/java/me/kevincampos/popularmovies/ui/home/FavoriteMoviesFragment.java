package me.kevincampos.popularmovies.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.database.MovieContract;
import me.kevincampos.popularmovies.ui.moviedetail.MovieDetailActivity;

public class FavoriteMoviesFragment extends BaseMovieListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int FAVORITE_MOVIES_LOADER_ID = 1;

    private MovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupAdapter();
        getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER_ID, null, this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupAdapter() {
        adapter = new MovieAdapter(getActivity(), new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                MovieDetailActivity.openMovieDetail(getActivity(), movie);
            }
        });
    }

    @Override
    public void loadData() {
        displayGrid();
    }

    @Override
    public MovieAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                MovieContract.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
