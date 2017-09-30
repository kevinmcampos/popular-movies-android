package me.kevincampos.popularmovies.data.api;

import android.content.Context;

import java.util.List;

import me.kevincampos.popularmovies.data.Movie;

public class MoviesDataManager {

    private final int INITIAL_PAGE = 1;

    private Context context;
    private String sortOrder;
    private DataLoadedCallback onLoadedCallback;

    private boolean isLoading;

    private int pageIndex = INITIAL_PAGE;

    public interface DataLoadedCallback {
        void onDataLoaded(List<Movie> movies);
        void onFailed(String errorText);
    }

    public MoviesDataManager(Context context, String sortOrder, DataLoadedCallback onLoadedCallback) {
        this.context = context;
        this.sortOrder = sortOrder;
        this.onLoadedCallback = onLoadedCallback;
    }

    public void loadPage() {
        if (isLoading) {
            return;
        }

        isLoading = true;

        FetchMoviesTask.FetchMoviesCallback fetchMoviesCallback = new FetchMoviesTask.FetchMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                pageIndex++;
                isLoading = false;
                onLoadedCallback.onDataLoaded(movies);
            }

            @Override
            public void onFailed(String errorText) {
                pageIndex = 1;
                isLoading = false;
                onLoadedCallback.onFailed(errorText);
            }
        };

        // TODO: Check for possible leaks
        new FetchMoviesTask(context, sortOrder, pageIndex, fetchMoviesCallback).execute();
    }

    public boolean isDataLoading() {
        return isLoading;
    }

}
