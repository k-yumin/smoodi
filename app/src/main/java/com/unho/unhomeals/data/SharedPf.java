package com.unho.unhomeals.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class SharedPf {

    public static final String NAME = "unhosharedpreferences";

    public static void put(AppCompatActivity activity, String s, boolean b) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(s, b);
        editor.apply();
    }

    public static void put(AppCompatActivity activity, String s, String s1) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(s, s1);
        editor.apply();
    }

    public static void put(AppCompatActivity activity, String s, Set<String> set) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putStringSet(s, set);
        editor.apply();
    }

    public static boolean getBoolean(AppCompatActivity activity, String s) {
        SharedPreferences preferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(s, false);
    }

    public static String getString(AppCompatActivity activity, String s) {
        SharedPreferences preferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getString(s, "");
    }

    public static Set<String> getStringSet(AppCompatActivity activity, String s) {
        SharedPreferences preferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getStringSet(s, new HashSet<>());
    }
}
