package me.kevincampos.popularmovies.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;
import me.kevincampos.popularmovies.ui.moviedetail.MovieDetailActivity;

public class PopularMoviesFragment extends BaseMovieListFragment {

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupAdapter() {
        moviesDataManager = new MoviesDataManager(getContext(), getActivity().getString(R.string.pref_order_most_popular_value), new MoviesDataManager.DataLoadedCallback() {
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
    public void onReachEnd() {
        if (!moviesDataManager.isDataLoading()) {
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
