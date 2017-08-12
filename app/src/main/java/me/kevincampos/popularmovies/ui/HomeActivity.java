package me.kevincampos.popularmovies.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;
import me.kevincampos.popularmovies.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setSupportActionBar(binding.toolbar);
        TextView toolbarText = (TextView) binding.toolbar.getChildAt(0);
        toolbarText.setTextColor(Color.BLACK);
        toolbarText.setFontFeatureSettings("smcp");

        fixStatusBarPadding();

        moviesDataManager = new MoviesDataManager(getBaseContext(), new MoviesDataManager.DataLoadedCallback() {
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

        adapter = new MovieAdapter(this, moviesDataManager, new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                MovieDetailActivity.openMovieDetail(HomeActivity.this, movie);
            }
        });

        binding.moviesGrid.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesDataManager.checkIfSettingsHasChanged();

    }

    private void fixStatusBarPadding() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);

            View separator = findViewById(R.id.status_bar_padding);
            LinearLayout.LayoutParams separatorLayoutParams = (LinearLayout.LayoutParams) separator.getLayoutParams();
            separatorLayoutParams.height = statusBarHeight;
            separator.setLayoutParams(separatorLayoutParams);
        }
    }
}
