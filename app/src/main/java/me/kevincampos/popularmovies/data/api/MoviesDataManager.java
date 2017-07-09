package me.kevincampos.popularmovies.data.api;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.data.Movie;

public class MoviesDataManager implements DataLoader {

    private final int INITIAL_PAGE = 1;

    private Context context;
    private boolean isLoading;
    private int pageIndex = INITIAL_PAGE;

    private DataLoadedCallback onLoadedCallback;
    private List<DataLoadingCallbacks> loadingCallbacks;

    public interface DataLoadedCallback {
        void onDataLoaded(List<Movie> movies);
    }

    public MoviesDataManager(Context context, DataLoadedCallback onLoadedCallback) {
        this.context = context;
        this.onLoadedCallback = onLoadedCallback;
    }

    public void loadPage() {
        if (isLoading) {
            return;
        }

        isLoading = true;
        dispatchLoadingStartedCallbacks();

        FetchMoviesTask.FetchMoviesCallback fetchMoviesCallback = new FetchMoviesTask.FetchMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                pageIndex++;
                dispatchLoadingFinishedCallbacks();
                isLoading = false;
                onLoadedCallback.onDataLoaded(movies);
            }

            @Override
            public void onFailed() {
                // TODO: Handle errors
                dispatchLoadingFinishedCallbacks();
                isLoading = false;
            }
        };

        new FetchMoviesTask(context, pageIndex, fetchMoviesCallback).execute();
    }

    @Override
    public boolean isDataLoading() {
        return isLoading;
    }

    @Override
    public void registerCallback(DataLoadingCallbacks callbacks) {
        if (loadingCallbacks == null) {
            loadingCallbacks = new ArrayList<>(1);
        }
        loadingCallbacks.add(callbacks);
    }

    @Override
    public void unregisterCallback(DataLoadingCallbacks callback) {
        if (loadingCallbacks != null && loadingCallbacks.contains(callback)) {
            loadingCallbacks.remove(callback);
        }
    }

    private void dispatchLoadingStartedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;

        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataStartedLoading();
        }
    }

    private void dispatchLoadingFinishedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;

        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataFinishedLoading();
        }
    }
}
