package me.kevincampos.popularmovies.data.api;

public interface DataLoader {

    boolean isDataLoading();

    void registerCallback(DataLoadingCallbacks callbacks);

    void unregisterCallback(DataLoadingCallbacks callbacks);

}
