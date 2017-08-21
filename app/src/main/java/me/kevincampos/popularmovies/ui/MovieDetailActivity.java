package me.kevincampos.popularmovies.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.database.MovieContract;
import me.kevincampos.popularmovies.data.database.MovieUpdateService;
import me.kevincampos.popularmovies.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final int MOVIE_DETAIL_LOADER_ID = 0;

    private static final String MOVIE_PARCELABLE_KEY = "MOVIE_PARCELABLE_KEY";

    private boolean isFavorite;

    private ActivityMovieDetailBinding binding;

    public static void openMovieDetail(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARCELABLE_KEY, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fixToolbarMargin();

        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);

        Movie movie = getIntent().getParcelableExtra(MOVIE_PARCELABLE_KEY);
        bindViews(movie);
    }

    private void bindViews(Movie movie) {
        Picasso.with(getBaseContext())
                .load(movie.getBackdropURL())
                .into(binding.movieBackdrop);
        binding.movieBackdrop.setContentDescription(getString(R.string.movie_backdrop_content_description, movie.TITLE));

        Picasso.with(getBaseContext())
                .load(movie.getPosterURL())
                .into(binding.header.moviePoster);
        binding.header.moviePoster.setContentDescription(getString(R.string.movie_poster_content_description, movie.TITLE));

        String itemTitle = movie.TITLE;
        binding.header.movieTitle.setText(itemTitle);
        binding.header.movieTitle.setTextColor(Color.BLACK);
        binding.header.movieTitle.setFontFeatureSettings("smcp");

        binding.header.movieGenres.setText(movie.GENRES);

        String releaseDateFormatted = movie.getReleaseDateFormatted();
        binding.header.movieReleaseDate.setText(releaseDateFormatted);

        CharSequence ratingFormatted = formatRating(String.valueOf(movie.VOTE_AVERAGE));
        binding.header.movieRating.setText(ratingFormatted);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getBaseContext(), getSupportFragmentManager(), movie);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.viewPager.reMeasureCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
    }

    private void toggleFavorite() {
        Movie movie = getIntent().getParcelableExtra(MOVIE_PARCELABLE_KEY);
        final String actionFeedbackText;
        if (isFavorite) {
            Uri movieUri = MovieContract.buildMovieUriWithId(movie.ID);
            MovieUpdateService.unfavoriteMovie(getBaseContext(), movieUri);
            actionFeedbackText = "Removed from favorites.";
            isFavorite = false;
        } else {
            MovieUpdateService.favoriteMovie(getBaseContext(), movie);
            actionFeedbackText = "Marked as favorite!";
            isFavorite = true;
        }

        Toast.makeText(MovieDetailActivity.this, actionFeedbackText, Toast.LENGTH_SHORT).show();
        refreshMovieFavoriteStatus();
    }

    public CharSequence formatRating(String rating) {
        Spannable ratingSpan = new SpannableString(rating);
        ratingSpan.setSpan(Typeface.BOLD, 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ratingSpan.setSpan(new RelativeSizeSpan(1.3f), 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ratingSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, rating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return TextUtils.concat(ratingSpan, " / 10 (TMDB)");
    }

    public void fixToolbarMargin() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int titleBarHeight = getResources().getDimensionPixelSize(resourceId);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            params.topMargin = titleBarHeight;
        }
    }

    private void refreshMovieFavoriteStatus() {
        int favoriteStatusResId = isFavorite ? R.drawable.ic_marked_as_favorite : R.drawable.ic_not_marked_as_favorite;
        binding.fabFavorite.setImageDrawable(getResources().getDrawable(favoriteStatusResId, getBaseContext().getTheme()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (getIntent() == null) {
            throw new IllegalArgumentException();
        }

        Movie movie = getIntent().getParcelableExtra(MOVIE_PARCELABLE_KEY);
        Uri movieUri = MovieContract.buildMovieUriWithId(movie.ID);

        return new CursorLoader(getBaseContext(),
                movieUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        isFavorite = data.moveToFirst();
        refreshMovieFavoriteStatus();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing
    }

    private class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private final static int PAGE_COUNT = 3;

        private final Context context;
        private final Movie movie;

        public FragmentPagerAdapter(Context context, FragmentManager fm, Movie movie) {
            super(fm);
            this.context = context;
            this.movie = movie;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return OverviewFragment.newInstance(movie);
                }
                case 1: {
                    return TrailersFragment.newInstance(movie.ID);
                }
                case 2: {
                    return ReviewsFragment.newInstance(movie.ID);
                }
                default:
                    throw new RuntimeException("Invalid position for FragmentPagerAdapter: " + position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return context.getString(R.string.movie_overview);
                }
                case 1: {
                    return context.getString(R.string.movie_trailer);
                }
                case 2: {
                    return context.getString(R.string.movie_review);
                }
                default:
                    throw new RuntimeException("Invalid position for FragmentPagerAdapter: " + position);
            }
        }

    }

}
