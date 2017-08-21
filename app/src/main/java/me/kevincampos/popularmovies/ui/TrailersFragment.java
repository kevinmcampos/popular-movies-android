package me.kevincampos.popularmovies.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.databinding.FragmentTrailersBinding;

public class TrailersFragment extends Fragment {

    public static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";

    private Long movieId;

    public static TrailersFragment newInstance(Long movieId) {
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID_KEY, movieId);
        TrailersFragment fragment = new TrailersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieId = getArguments().getLong(MOVIE_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTrailersBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_trailers, container, false);
        return binding.getRoot();
    }

}
