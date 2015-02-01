package fr.soundfit.android.utils;

import android.content.Context;
import android.content.Intent;

import fr.soundfit.android.ui.activity.HomeActivity;
import fr.soundfit.android.ui.activity.WelcomeActivity;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.utils
 * By Donovan on 08/01/2015.
 */
public class IntentUtils {

    public static void launchHomeActivity(Context context){
        if(context == null)
            return;
        Intent i = new Intent(context, HomeActivity.class);
        context.startActivity(i);
    }

    public static void launchWelcomeActivity(Context context){
        if(context == null)
            return;
        Intent i = new Intent(context, WelcomeActivity.class);
        context.startActivity(i);
    }
}
