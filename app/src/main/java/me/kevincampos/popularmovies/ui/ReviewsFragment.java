package me.kevincampos.popularmovies.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.databinding.FragmentReviewsBinding;

public class ReviewsFragment extends Fragment {

    public static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";

    private long movieId;

    public static ReviewsFragment newInstance(Long movieId) {
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID_KEY, movieId);
        ReviewsFragment fragment = new ReviewsFragment();
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
        FragmentReviewsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_reviews, container, false);
        return binding.getRoot();
    }

}
