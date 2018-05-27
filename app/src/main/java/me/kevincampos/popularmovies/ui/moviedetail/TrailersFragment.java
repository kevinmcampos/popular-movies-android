package me.kevincampos.popularmovies.ui.moviedetail;

import android.content.Intent;
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
import me.kevincampos.popularmovies.data.YoutubeVideo;
import me.kevincampos.popularmovies.data.api.FetchTrailersTask;
import me.kevincampos.popularmovies.databinding.FragmentTrailersBinding;

public class TrailersFragment extends Fragment {

    public static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";

    private Long movieId;

    private FetchTrailersTask fetchTrailersTask;
    private FragmentTrailersBinding binding;

    private VideoAdapter adapter;

    public static TrailersFragment newInstance(Long movieId) {
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID_KEY, movieId);
        TrailersFragment fragment = new TrailersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_trailers, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieId = getArguments().getLong(MOVIE_ID_KEY);

        adapter = new VideoAdapter(getActivity(), new VideoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideo youtubeVideo) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeVideo.getVideoURL());
                getActivity().startActivity(youtubeIntent);
            }
        });

        binding.trailerList.setAdapter(adapter);
        binding.trailerList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        binding.noInternetContainer.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        fetchTrailers();
    }

    private void fetchTrailers() {
        fetchTrailersTask = new FetchTrailersTask(getActivity(), movieId, new FetchTrailersTask.FetchTrailersCallback() {
            @Override
            public void onSuccess(List<YoutubeVideo> trailers) {
                binding.loading.setVisibility(View.GONE);
                adapter.swapTrailers(trailers);
            }

            @Override
            public void onFailed(String errorText) {
                displayError(errorText);
            }
        });
        fetchTrailersTask.execute();
    }

    protected void displayError(String errorText) {
        binding.loading.setVisibility(View.GONE);
        binding.noInternetContainer.container.setVisibility(View.VISIBLE);
        binding.noInternetContainer.errorText.setText(errorText);
    }

    private void retry() {
        binding.loading.setVisibility(View.VISIBLE);
        binding.noInternetContainer.container.setVisibility(View.GONE);
        fetchTrailers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchTrailersTask != null && !fetchTrailersTask.isCancelled()) {
            fetchTrailersTask.cancel(true);
        }
    }
}
