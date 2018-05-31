package me.kevincampos.popularmovies.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;
import me.kevincampos.popularmovies.ui.moviedetail.MovieDetailActivity;

public class TopRatedMoviesFragment extends BaseMovieListFragment {

    private static final String MOVIES_KEY = "MOVIES_KEY";

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupAdapter() {
        moviesDataManager = new MoviesDataManager(getContext(), getActivity().getString(R.string.pref_order_top_rated_value), new MoviesDataManager.DataLoadedCallback() {
            @Override
            public void onDataLoaded(List<Movie> movies) {
                adapter.addData(movies);
                displayGrid();
            }

            @Override
            public void onFailed(String errorText) {
                adapter.clear();
                displayError(errorText);
            }
        });

        adapter = new MovieAdapter(getActivity(), new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                MovieDetailActivity.openMovieDetail(getActivity(), movie);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<? extends Parcelable>) adapter.getMovies());
        moviesDataManager.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                adapter.clear();
                adapter.addData(movies);
            }
            moviesDataManager.restoreInstanceState(savedInstanceState);
        }

        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onReachEnd() {
        if (!moviesDataManager.isLoading()) {
            moviesDataManager.loadPage();
        }
    }

    @Override
    public MovieAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void loadData() {
        moviesDataManager.loadPage();
    }
}
