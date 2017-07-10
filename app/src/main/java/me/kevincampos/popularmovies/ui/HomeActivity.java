package me.kevincampos.popularmovies.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movies_grid)
    RecyclerView moviesGrid;

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        TextView toolbarText = (TextView) toolbar.getChildAt(0);
        toolbarText.setTextColor(Color.BLACK);
        toolbarText.setFontFeatureSettings("smcp");

        fixStatusBarPadding();

        moviesDataManager = new MoviesDataManager(getBaseContext(), new MoviesDataManager.DataLoadedCallback() {
            @Override
            public void onDataLoaded(List<Movie> movies) {
                adapter.addData(movies);
            }
        });

        adapter = new MovieAdapter(this, moviesDataManager);

        moviesGrid.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesGrid.setLayoutManager(layoutManager);
        moviesGrid.addOnScrollListener(new InfiniteScrollListener(layoutManager, moviesDataManager) {
            @Override
            public void onLoadMore() {
                moviesDataManager.loadPage();
            }
        });
        moviesGrid.setHasFixedSize(true);

        moviesDataManager.loadPage();
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
