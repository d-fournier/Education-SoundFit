package fr.soundfit.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.soundfit.android.ui.fragment.LoginFragment;
import fr.soundfit.android.ui.fragment.LevelChooserFragment;
import fr.soundfit.android.ui.fragment.MusicPreferenceChooserFragment;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.adapter
 * By Donovan on 08/01/2015.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {

    protected final static int WELCOME_SCREEN_NUMBER = 3;
    protected final static int LOGIN_SCREEN_INDEX = 0;
    protected final static int LEVEL_SCREEN_INDEX = 1;
    protected final static int MUSIC_PREFERENCE_SCREEN_INDEX = 2;

    protected int mCurrentProgress = 1;
    protected FragmentManager mFragmentManager;

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        String tag;
        switch (position){
            case LEVEL_SCREEN_INDEX:
                tag = LevelChooserFragment.TAG;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = LevelChooserFragment.newInstance();
                }
                break;
            case MUSIC_PREFERENCE_SCREEN_INDEX:
                tag = MusicPreferenceChooserFragment.TAG;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = MusicPreferenceChooserFragment.newInstance();
                }
                break;
            case LOGIN_SCREEN_INDEX:
            default:
                tag = LoginFragment.TAG;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = LoginFragment.newInstance();
                }
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int progress){
        if(progress<0){
            mCurrentProgress = 0;
        } else if(progress>WELCOME_SCREEN_NUMBER) {
            mCurrentProgress = WELCOME_SCREEN_NUMBER;
        } else {
            mCurrentProgress = progress;
        }
        notifyDataSetChanged();
    }

    public int getCurrentProgress(){
        return mCurrentProgress;
    }

    public int getTotalPages(){
        return WELCOME_SCREEN_NUMBER;
    }

    public void enableNextPage(){
        if(mCurrentProgress < WELCOME_SCREEN_NUMBER){
            ++mCurrentProgress;
        }
        notifyDataSetChanged();
    }

}
