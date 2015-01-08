package fr.soundfit.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.utils.IntentUtils;
import fr.soundfit.android.ui.utils.PrefUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.activity
 * By Donovan on 07/01/2015.
 */
public class SplashscreenActivity extends GenericActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_ANIMATION = 2000;
    private static final int SPLASH_TIME_OUT = 5000;
    private static final int FADE_ANIMATION_TIME = 1000;
    private static final int TRANSLATION_ANIMATION_TIME = 500;

    protected ImageView mLogoIV;
    protected ImageView mImageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        mLogoIV = (ImageView) findViewById(R.id.splashscreen_logo);
        mImageIV = (ImageView) findViewById(R.id.splashscreen_image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing() && mImageIV != null){
                    mImageIV.setVisibility(View.VISIBLE);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setDuration(FADE_ANIMATION_TIME);
                    mImageIV.startAnimation(alphaAnimation);

                    float deltaY = (mLogoIV.getHeight()+mImageIV.getHeight()+
                            SplashscreenActivity.this.getResources().getDimension(R.dimen.splachscreen_space))/2
                            - (-mLogoIV.getHeight()/2);
                    TranslateAnimation translateAnimation =
                            new TranslateAnimation(0, 0, deltaY, 0);
                    translateAnimation.setDuration(TRANSLATION_ANIMATION_TIME);
                    mLogoIV.startAnimation(translateAnimation);
                }
            }
        }, SPLASH_TIME_ANIMATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()){
                    if(shouldLaunchWelcomeActivity()){
                        IntentUtils.launchWelcomeActivity(SplashscreenActivity.this);
                    } else {
                        IntentUtils.launchHomeActivity(SplashscreenActivity.this);
                    }
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }



    protected boolean shouldLaunchWelcomeActivity(){
        return !PrefUtils.isUserConnected(this) || PrefUtils.getUserLevel(this) == -1
                || PrefUtils.getUserMusicPreference(this) == -1;
    }


}