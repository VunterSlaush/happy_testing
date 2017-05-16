package mota.dev.happytesting.utils;

/**
 * Created by Slaush on 15/05/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import mota.dev.happytesting.Consts;

public class PreferencesHelper
{

    public static void deleteKey(Context context, String key) {
        if (context != null)
            getPreferences(context).remove(key);
    }


    public static void writeBoolean(Context context, String key, boolean value) {
        if (context != null)
            getPreferences(context).putBoolean(key, value);
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return context != null && getPreferences(context).getBoolean(key, defValue);
    }


    public static void writeInteger(Context context, String key, int value) {
        if (context != null)
            getPreferences(context).putInt(key, value);

    }

    public static int readInteger(Context context, String key, int defValue) {
        return context != null ? getPreferences(context).getInt(key, defValue) : -1;
    }


    public static void writeString(Context context, String key, String value) {
        if (context != null)
            getPreferences(context).putString(key, value);
    }

    public static String readString(Context context, String key, String defValue) {
        return context != null ? getPreferences(context).getString(key, defValue) : "";
    }

    public static void writeDouble(Context context, String key, Double value) {
        if (context != null)
            getPreferences(context).putDouble(key, value);
    }

    public static Double readDouble(Context context, String key, Double defValue) {
        return context != null ? getPreferences(context).getDouble(key, defValue) : null;
    }

    private static SecurePreferences getPreferences(Context context) {
        return new SecurePreferences(context, "5c0d3349dc674fe9d0");

    }

    public static void putStringArray(Context context, String key, Set<String> set)
    {
        if (context != null)
            context.getSharedPreferences(Consts.SHARED_PREF_NAME, SecurePreferences.MODE).edit().putStringSet(key, set).commit();
    }

    public static Set<String> getStringArray(Context context, String key) {
        if (context != null)
            return context.getSharedPreferences(Consts.SHARED_PREF_NAME, SecurePreferences.MODE).getStringSet(key, null);
        else
            return null;
    }


}