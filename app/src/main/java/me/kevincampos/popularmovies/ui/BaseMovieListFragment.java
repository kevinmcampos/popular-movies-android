package me.kevincampos.popularmovies.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;
import me.kevincampos.popularmovies.databinding.FragmentMovieListBinding;

public abstract class BaseMovieListFragment extends Fragment {

    private FragmentMovieListBinding binding;

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        moviesDataManager = new MoviesDataManager(getContext(), new MoviesDataManager.DataLoadedCallback() {
            @Override
            public void onDataLoaded(List<Movie> movies) {
                adapter.addData(movies);
                binding.loading.setVisibility(View.GONE);
                binding.moviesGrid.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDataChange() {
                adapter.clear();
                binding.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String errorText) {
                adapter.clear();
                displayError(errorText);
            }
        });

        adapter = new MovieAdapter(getActivity(), moviesDataManager, new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                MovieDetailActivity.openMovieDetail(getActivity(), movie);
            }
        });

        binding.moviesGrid.setAdapter(adapter);
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }
        binding.moviesGrid.setLayoutManager(layoutManager);
        binding.moviesGrid.addOnScrollListener(new InfiniteScrollListener(layoutManager, moviesDataManager) {
            @Override
            public void onLoadMore() {
                moviesDataManager.loadPage();
            }
        });
        binding.moviesGrid.setHasFixedSize(true);

        moviesDataManager.loadPage();

        binding.noInternetContainer.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
    }


    private void displayError(String errorText) {
        binding.loading.setVisibility(View.GONE);
        binding.moviesGrid.setVisibility(View.GONE);
        binding.noInternetContainer.container.setVisibility(View.VISIBLE);
        binding.noInternetContainer.errorText.setText(errorText);
    }

    private void retry() {
        binding.loading.setVisibility(View.VISIBLE);
        binding.noInternetContainer.container.setVisibility(View.GONE);
        moviesDataManager.loadPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        moviesDataManager.checkIfSettingsHasChanged();
    }
}
