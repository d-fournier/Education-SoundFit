package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.LoginFragment;
import fr.soundfit.android.ui.fragment.LevelChooserFragment;
import fr.soundfit.android.ui.fragment.MusicPreferenceChooserFragment;
import fr.soundfit.android.ui.fragment.TutorialFragment;

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
    protected Context mContext;

    protected int mTutorialNumber;

    public WelcomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
        mTutorialNumber = mContext.getResources().getStringArray(R.array.tutorial_item).length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        String tag;
        if(position == (mTutorialNumber + LEVEL_SCREEN_INDEX)){
            tag = LevelChooserFragment.TAG;
            frag = mFragmentManager.findFragmentByTag(tag);
            if(frag == null){
                frag = LevelChooserFragment.newInstance(false);
            }
        } else if(position == (mTutorialNumber + MUSIC_PREFERENCE_SCREEN_INDEX)){
            tag = MusicPreferenceChooserFragment.TAG;
            frag = mFragmentManager.findFragmentByTag(tag);
            if(frag == null){
                frag = MusicPreferenceChooserFragment.newInstance(false);
            }
        } else if(position == (mTutorialNumber + LOGIN_SCREEN_INDEX)){
            tag = LoginFragment.TAG;
            frag = mFragmentManager.findFragmentByTag(tag);
            if(frag == null){
                frag = LoginFragment.newInstance();
            }
        } else {
            tag = TutorialFragment.TAG+position;
            frag = mFragmentManager.findFragmentByTag(tag);
            if(frag == null){
                frag = TutorialFragment.newInstance(mContext.getResources().getStringArray(R.array.tutorial_item)[position]);
            }
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
        } else if(progress>getTotalPages()) {
            mCurrentProgress = getTotalPages();
        } else {
            mCurrentProgress = progress;
        }
        notifyDataSetChanged();
    }

    public int getCurrentProgress(){
        return mCurrentProgress;
    }

    public int getTotalPages(){
        return WELCOME_SCREEN_NUMBER + mTutorialNumber;
    }

    public void enableNextPage(){
        if(mCurrentProgress < getTotalPages()){
            ++mCurrentProgress;
        }
        notifyDataSetChanged();
    }

}
