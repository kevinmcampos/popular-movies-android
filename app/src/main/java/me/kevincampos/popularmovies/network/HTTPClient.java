package me.kevincampos.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public class HTTPClient {

    private static final String TAG = "HTTPClient";

    @CheckResult
    @NonNull
    @WorkerThread
    public HTTPResponse execute(@NonNull Context context, @NonNull String method, @NonNull String urlString) throws NotConnectedException, InternalErrorException {
        Log.d(TAG, "Trying to requesting " + urlString + " with method " + method);

        HttpURLConnection urlConnection = null;
        try {
            if (!isInternetConnected(context)) {
                throw new NotConnectedException();
            }

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            InputStream responseInputStream = new BufferedInputStream((responseCode >= 400) ?
                    urlConnection.getErrorStream() : urlConnection.getInputStream());

            HTTPResponse httpResponse = HTTPResponse.with(responseCode, responseInputStream);

            Log.d(TAG, "Success with status code=" + responseCode + " and response=" + httpResponse.getResponse());
            return httpResponse;
        } catch (UnknownHostException | SocketTimeoutException | ConnectException e) {
            Log.d(TAG, "Failed with connection lost.");
            e.printStackTrace();
            throw new NotConnectedException();
        } catch (IOException e) { // MalformedURLException, ProtocolException, UnsupportedEncodingException and unknown others
            Log.e(TAG, "Failed with IOException");
            e.printStackTrace();
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            throw new InternalErrorException();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class NotConnectedException extends Exception {
    }

    public class InternalErrorException extends Exception {
    }
}
