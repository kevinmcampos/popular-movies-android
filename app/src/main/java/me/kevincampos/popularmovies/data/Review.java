package me.kevincampos.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    private final String ID;
    public final String AUTHOR;
    public final String CONTENT;

    public Review(String id, String author, String content) {
        this.ID = id;
        this.AUTHOR = author;
        this.CONTENT = content;
    }

    public static Review fromJSON(JSONObject reviewJSON) throws JSONException {
        String id = reviewJSON.getString("id");
        String author = reviewJSON.getString("author");
        String content = reviewJSON.getString("content");
        return new Review(id, author, content);
    }

}
