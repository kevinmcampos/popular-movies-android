package me.kevincampos.popularmovies.ui.home;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.databinding.FragmentMovieListBinding;
import me.kevincampos.popularmovies.ui.widget.InfiniteScrollListener;

public abstract class BaseMovieListFragment extends Fragment {

    private FragmentMovieListBinding binding;

    public abstract MovieAdapter getAdapter();

    public abstract void loadData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.moviesGrid.setAdapter(getAdapter());

        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }
        binding.moviesGrid.setHasFixedSize(true);
        binding.moviesGrid.setLayoutManager(layoutManager);
        binding.moviesGrid.addOnScrollListener(new InfiniteScrollListener(layoutManager) {
            @Override
            public void onReachEnd() {
                BaseMovieListFragment.this.onReachEnd();
            }
        });

        binding.noInternetContainer.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        loadData();
    }

    protected void onReachEnd() {
    }

    protected void displayGrid() {
        binding.loading.setVisibility(View.GONE);
        binding.moviesGrid.setVisibility(View.VISIBLE);
    }

    protected void displayError(String errorText) {
        binding.loading.setVisibility(View.GONE);
        binding.moviesGrid.setVisibility(View.GONE);
        binding.noInternetContainer.container.setVisibility(View.VISIBLE);
        binding.noInternetContainer.errorText.setText(errorText);
    }

    private void retry() {
        binding.loading.setVisibility(View.VISIBLE);
        binding.noInternetContainer.container.setVisibility(View.GONE);
        loadData();
    }
}
