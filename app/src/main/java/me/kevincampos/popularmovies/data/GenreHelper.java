package me.kevincampos.popularmovies.data;

import java.util.HashMap;

public class GenreHelper {

    private static HashMap<Integer, String> genresMap = new HashMap<>();

    static {
        genresMap.put(28, "Action");
        genresMap.put(12, "Adventure");
        genresMap.put(16, "Animation");
        genresMap.put(35, "Comedy");
        genresMap.put(80, "Crime");
        genresMap.put(99, "Documentary");
        genresMap.put(18, "Drama");
        genresMap.put(10751, "Family");
        genresMap.put(14, "Fantasy");
        genresMap.put(36, "History");
        genresMap.put(27, "Horror");
        genresMap.put(10402, "Music");
        genresMap.put(9648, "Mystery");
        genresMap.put(10749, "Romance");
        genresMap.put(878, "Science Fiction");
        genresMap.put(10770, "TV Movie");
        genresMap.put(53, "Thriller");
        genresMap.put(10752, "War");
        genresMap.put(37, "Western");
    }

    public static String getGenreById(Integer genreId) {
        return genresMap.get(genreId);
    }

}
