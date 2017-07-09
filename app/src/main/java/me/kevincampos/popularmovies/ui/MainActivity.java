package me.kevincampos.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movies_grid)
    private RecyclerView moviesGrid;

    private MoviesDataManager moviesDataManager;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
}
