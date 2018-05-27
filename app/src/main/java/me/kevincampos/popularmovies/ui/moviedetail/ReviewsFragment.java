package me.kevincampos.popularmovies.ui.moviedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Review;
import me.kevincampos.popularmovies.data.api.FetchReviewsTask;
import me.kevincampos.popularmovies.databinding.FragmentReviewsBinding;

public class ReviewsFragment extends Fragment {

    public static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";

    private long movieId;

    private FetchReviewsTask fetchReviewsTask;
    private FragmentReviewsBinding binding;

    private ReviewAdapter adapter;

    public static ReviewsFragment newInstance(Long movieId) {
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID_KEY, movieId);
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_reviews, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieId = getArguments().getLong(MOVIE_ID_KEY);

        adapter = new ReviewAdapter(getActivity());

        binding.reviewList.setAdapter(adapter);
        binding.reviewList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        fetchReviewsTask = new FetchReviewsTask(getActivity(), movieId, new FetchReviewsTask.FetchReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                adapter.swapReviews(reviews);
            }

            @Override
            public void onFailed(String errorText) {
                // TODO: Display error
            }
        });
        fetchReviewsTask.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchReviewsTask != null && !fetchReviewsTask.isCancelled()) {
            fetchReviewsTask.cancel(true);
        }
    }

}
