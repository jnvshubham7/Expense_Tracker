package com.example.expensetracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences_Util {
    private static Preferences_Util instance;
    private final SharedPreferences SP;

    private Preferences_Util(Context mContext) {
        SP = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static Preferences_Util getInstance(Context context) {
        if (instance == null) {
            synchronized (Preferences_Util.class) {
                if (instance == null)
                    instance = new Preferences_Util(context);
            }
        }
        return instance;
    }

    public SharedPreferences.Editor getEditor() {
        return SP.edit();
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return SP.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return SP.getInt(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return SP.getBoolean(key, defValue);
    }


    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    public void clearPreferences() {
        getEditor().clear().commit();
    }

}
