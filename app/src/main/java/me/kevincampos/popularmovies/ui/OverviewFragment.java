package me.kevincampos.popularmovies.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.databinding.FragmentOverviewBinding;

public class OverviewFragment extends Fragment {

    public static final String MOVIE_KEY = "MOVIE_KEY";

    private Movie movie;

    public static OverviewFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_KEY, movie);
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable(MOVIE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentOverviewBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_overview, container, false);
        binding.overview.setText(movie.OVERVIEW);
        return binding.getRoot();
    }

}
