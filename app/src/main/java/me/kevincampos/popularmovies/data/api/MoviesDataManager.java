package me.kevincampos.popularmovies.data.api;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import me.kevincampos.popularmovies.data.Movie;

public class MoviesDataManager {

    private static final String PAGE_INDEX_KEY = "PAGE_INDEX";
    private static final String SORT_ORDER_KEY = "SORT_ORDER";

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

    public boolean isLoading() {
        return isLoading;
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

    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(PAGE_INDEX_KEY) && savedInstanceState.containsKey(SORT_ORDER_KEY)) {
            pageIndex = savedInstanceState.getInt(PAGE_INDEX_KEY);
            sortOrder = savedInstanceState.getString(SORT_ORDER_KEY);
        }
    }

    public void saveInstanceState(Bundle outState) {
        outState.putInt(PAGE_INDEX_KEY, pageIndex);
        outState.putString(SORT_ORDER_KEY, sortOrder);
    }
}
