package com.bijana.doms.apps.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import com.bijana.doms.apps.AddDoMs;
import com.bijana.doms.apps.MainActivity;

public class SharePreferenceData {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_ID = "id";

    public static void saveId(Context context, String id, String balance) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public static String getId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, "");
    }

}
