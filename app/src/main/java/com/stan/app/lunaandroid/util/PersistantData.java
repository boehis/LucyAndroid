package com.stan.app.lunaandroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.HashSet;
import java.util.Set;

public class PersistantData {
    public static final String MY_LUNAS = "com.stan.app.lunaandroid.MY_LUNAS";
    public static final String CURRENT_LUNAS = "com.stan.app.lunaandroid.CURRENT_LUNAS";
    public static final String CONNECT_AUTOMATICALLY = "com.stan.app.lunaandroid.CONNECT_AUTOMATICALLY";

    public static String getString(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPref.getString(key,"");
    }
    public static boolean getBoolean(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key,false);
    }
    public static Set<String> getStrings(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPref.getStringSet(key,new HashSet<String>());
    }
    public static void update(String key,String value, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void update(String key,boolean value, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void update(String key, HashSet<String> values, Context context) {
        Set<String> all = getStrings(key,context);
        all.addAll(values);
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, all);
        editor.apply();
    }
    public static void set(String key, HashSet<String> values, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, values);
        editor.apply();
    }
}
