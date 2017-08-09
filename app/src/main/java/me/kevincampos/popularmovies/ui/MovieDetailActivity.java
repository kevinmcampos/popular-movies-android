package me.kevincampos.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_PARCELABLE_KEY = "MOVIE_PARCELABLE_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.movie_backdrop)
    ImageView backdropImageView;

    @BindView(R.id.movie_poster)
    ImageView posterImageView;

    public static void openMovieDetail(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARCELABLE_KEY, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fixToolbarMargin();

        Movie movie = getIntent().getParcelableExtra(MOVIE_PARCELABLE_KEY);
        bindViews(movie);
    }

    private void bindViews(Movie movie) {
        Picasso.with(getBaseContext())
                .load(movie.getBackdropURL())
                .into(backdropImageView);
        backdropImageView.setContentDescription(getString(R.string.movie_backdrop_content_description, movie.TITLE));

        Picasso.with(getBaseContext())
                .load(movie.getPosterURL())
                .into(posterImageView);
        posterImageView.setContentDescription(getString(R.string.movie_poster_content_description, movie.TITLE));

        String itemTitle = movie.TITLE;
        title.setText(itemTitle);
        title.setTextColor(Color.BLACK);
        title.setFontFeatureSettings("smcp");
    }

    public void fixToolbarMargin() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int titleBarHeight = getResources().getDimensionPixelSize(resourceId);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            params.topMargin = titleBarHeight;
        }
    }

}
