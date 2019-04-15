package com.locus.locusdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {


    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);


    }


    public static void saveHomeTabData(Context context, boolean saved) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean("data_updated", saved);
        editor.apply();
    }


    public static boolean getDataSaved(Context context) {
        return getSharedPreference(context).getBoolean("data_updated", false);
    }

}
