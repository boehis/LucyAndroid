package com.stan.app.lunaandroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PersistantData {
    private static final String PERSISTANT_DATA = "com.stan.app.lunaandroid.PERSISTANT_DATA";

    public static final String MY_LUNAS = "com.stan.app.lunaandroid.MY_LUNAS";
    public static final String CURRENT_LUNAS = "com.stan.app.lunaandroid.CURRENT_LUNAS";
    public static final String CONNECT_AUTOMATICALLY = "com.stan.app.lunaandroid.CONNECT_AUTOMATICALLY";
    public static final String LAST_PICKED_COLOR = "com.stan.app.lunaandroid.LAST_PICKED_COLOR";
    public static final String PICKED_COLOR_LIST = "com.stan.app.lunaandroid.PICKED_COLOR_LIST";


    private static SharedPreferences sharedPreferences;

    private static Set<String> myLunas;
    private static Set<String> currentLunas;
    private static Boolean connectAutomatically;
    private static Integer lastPickedColor;
    private static ArrayList<Integer> pickedColorList;



    private static SharedPreferences getSharedPref(Context context) {
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PERSISTANT_DATA, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static Set<String> getMyLunas(Context context) {
        if(myLunas == null){
            myLunas = getStrings(MY_LUNAS,context);
        }
        return myLunas;
    }

    public static Set<String> getCurrentLunas(Context context) {
        if(currentLunas == null){
            currentLunas = getStrings(CURRENT_LUNAS,context);
        }
        return currentLunas;
    }

    public static boolean isConnectAutomatically(Context context) {
        if(connectAutomatically == null){
            connectAutomatically = getBoolean(CONNECT_AUTOMATICALLY, context);
        }
        return connectAutomatically;
    }

    public static int getLastPickedColor(Context context) {
        if(lastPickedColor == null) {
            lastPickedColor = getInteger(LAST_PICKED_COLOR,context);
        }
        return lastPickedColor;
    }

    public static ArrayList<Integer> getPickedColorList(Context context) {
        if(pickedColorList == null){
            Set<String> elements = getStrings(PICKED_COLOR_LIST,context);
            ArrayList<Integer> converted  = new ArrayList<>();
            for (String element : elements) {
                converted.add(Integer.valueOf(element));
            }
            pickedColorList = converted;
        }
        return pickedColorList;
    }

    public static void setMyLunas(Set<String> myLunas) {
        PersistantData.myLunas = myLunas;
    }
    public static void addToMyLunas(String value) {
        PersistantData.myLunas.add(value);
    }
    public static void addToMyLunas(Set<String> values) {
        PersistantData.myLunas.addAll(values);
    }
    public static void removefromMyLunas(String value) {
        PersistantData.myLunas.remove(value);
    }

    public static void setCurrentLunas(Set<String> currentLunas) {
        PersistantData.currentLunas = currentLunas;
    }
    public static void addToCurrentLunas(String value) {
        PersistantData.currentLunas.add(value);
    }
    public static void addToCurrentLunas(Set<String> values) {
        PersistantData.currentLunas.addAll(values);
    }
    public static void removeFromCurrentLunas(String value) {
        PersistantData.currentLunas.remove(value);
    }

    public static void setConnectAutomatically(boolean connectAutomatically) {
        PersistantData.connectAutomatically = connectAutomatically;
    }

    public static void setLastPickedColor(int lastPickedColor) {
        PersistantData.lastPickedColor = lastPickedColor;
    }

    public static void setPickedColorList(ArrayList<Integer> pickedColorList) {
        PersistantData.pickedColorList = pickedColorList;
    }
    public static void addToPickedColorList(Integer value) {
        PersistantData.pickedColorList.add(value);
    }
    public static void addToPickedColorList(ArrayList<Integer> values) {
        PersistantData.pickedColorList.addAll(values);
    }
    public static void removefromPickedColorList(Integer value) {
        PersistantData.pickedColorList.remove(value);
    }

    public static void savePersistantValues(Context context) {
        context.getSharedPreferences(PERSISTANT_DATA, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(PersistantData.MY_LUNAS,getMyLunas(context))
                .putStringSet(PersistantData.CURRENT_LUNAS,getCurrentLunas(context))
                .putBoolean(PersistantData.CONNECT_AUTOMATICALLY, isConnectAutomatically(context))
                .putInt(PersistantData.LAST_PICKED_COLOR, getLastPickedColor(context))
                .putStringSet(PersistantData.PICKED_COLOR_LIST, Util.convertCollection(getPickedColorList(context)))
                .apply();
    }

    private static String getString(String key, Context context) {
        return getSharedPref(context).getString(key,"");
    }
    private static boolean getBoolean(String key, Context context) {
        return getSharedPref(context).getBoolean(key,false);
    }
    private static int getInteger(String key, Context context) {
        return getSharedPref(context).getInt(key,0);
    }
    private static Set<String> getStrings(String key, Context context) {
        return getSharedPref(context).getStringSet(key,new HashSet<String>());
    }
}
