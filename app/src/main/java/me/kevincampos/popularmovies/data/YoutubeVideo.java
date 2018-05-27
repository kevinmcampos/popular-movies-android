package me.kevincampos.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeVideo {

    private final static String YOUTUBE_SITE_TYPE = "YouTube";

    private final String KEY;
    public final String NAME;
    private final String TYPE;

    private YoutubeVideo(String KEY, String NAME, String TYPE) {
        this.KEY = KEY;
        this.NAME = NAME;
        this.TYPE = TYPE;
    }

    public Uri getVideoURL() {
        if (TYPE.equals(YOUTUBE_SITE_TYPE)) {
            return new Uri.Builder()
                    .scheme("https")
                    .authority("youtube.com")
                    .appendPath("watch")
                    .appendQueryParameter("v", KEY)
                    .build();
        }

        Log.e("YoutubeVideo", "Non youtube trailers are not implemented.");
        return new Uri.Builder().build();
    }

    public Uri getThumbnailURL() {
        if (TYPE.equals(YOUTUBE_SITE_TYPE)) {
            return new Uri.Builder()
                    .scheme("https")
                    .authority("img.youtube.com")
                    .appendPath("vi")
                    .appendPath(KEY)
                    .appendPath("hqdefault.jpg")
                    .build();
        }

        Log.e("YoutubeVideo", "Non youtube trailers are not implemented.");
        return new Uri.Builder().build();
    }

    public static YoutubeVideo fromJSON(JSONObject trailerJSON) throws JSONException {
        String youtubeKey = trailerJSON.getString("key");
        String title = trailerJSON.getString("name");
        String type = trailerJSON.getString("site");
        return new YoutubeVideo(youtubeKey, title, type);
    }
}
