package me.kevincampos.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.kevincampos.popularmovies.R;

public class PreferencesUtil {

    public static String getMoviesOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_order_key),
                context.getString(R.string.pref_order_default_value));
    }

}
