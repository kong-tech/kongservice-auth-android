package com.kongtech.service.sdk.auth.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

    public static final String TOKEN_INFO  = "TOKEN_INFO";

    public static void saveStringPreference(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("kong-service", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        try {
            editor.putString(key, KeyUtil.encrypt(context, value));
        } catch (Exception e) {
            e.printStackTrace();
            editor.putString(key, value);
        }

        editor.commit();
    }

    public static void saveIntPreference(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences("kong-service", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int loadIntPreference(Context context, String key) {
        return loadIntPreference(context, key, 0);
    }

    public static String loadStringPreference(Context context, String key) {
        return loadStringPreference(context, key, "");
    }

    public static int loadIntPreference(Context context, String key, int defValue) {
        SharedPreferences pref = context.getSharedPreferences("kong-service", context.MODE_PRIVATE);
        return pref.getInt(key, defValue);
    }

    public static String loadStringPreference(Context context, String key, String defValue) {
        SharedPreferences pref = context.getSharedPreferences("kong-service", context.MODE_PRIVATE);
        String value = pref.getString(key, defValue);
        try {
            return KeyUtil.decrypt(context, value);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }
}
