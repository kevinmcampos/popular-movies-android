package me.kevincampos.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.kevincampos.popularmovies.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_PARCELABLE = "MOVIE";

    private Movie movie;

    public static void openMovieDetail(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARCELABLE, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = getIntent().getParcelableExtra(MOVIE_PARCELABLE);
    }
}
