package fr.soundfit.android.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.utils
 * By Donovan on 08/01/2015.
 */
public class PrefUtils {


    private static final String PREF_USER_IS_CONNECTED = "PREF_USER_IS_CONNECTED";
    private static final String PREF_USER_LEVEL = "PREF_USER_LEVEL";
    private static final String PREF_USER_MUSIC_PREFERENCE = "PREF_USER_MUSIC_PREFERENCE";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";


    public static boolean isDrawerLearned(Context context){
        if(context == null)
            return false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
    }

    public static void setDrawerLearned(Context context, boolean value){
        if(context == null)
            return ;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, value).apply();
    }

    public static boolean isUserConnected(Context context){
        if(context == null)
            return false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_USER_IS_CONNECTED, false);
    }

    public static void setUserConnected(Context context, boolean value){
        if(context == null)
            return ;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_USER_IS_CONNECTED, value).apply();
    }

    public static int getUserLevel(Context context){
        if(context == null)
            return -1;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_USER_LEVEL, -1);
    }

    public static void setUserLevel(Context context, int value){
        if(context == null)
            return ;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_USER_LEVEL, value).apply();
    }

    public static int getUserMusicPreference(Context context){
        if(context == null)
            return -1;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_USER_MUSIC_PREFERENCE, -1);
    }

    public static void setUserMusicPreference(Context context, int value){
        if(context == null)
            return ;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_USER_MUSIC_PREFERENCE, value).apply();
    }


}
