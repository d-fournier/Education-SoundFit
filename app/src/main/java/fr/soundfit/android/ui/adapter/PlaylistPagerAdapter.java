package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.PlaylistListFragment;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.adapter
 * By Donovan on 01/02/2015.
 */
public class PlaylistPagerAdapter extends FragmentPagerAdapter {

    protected final static int TAB_NUMBER = 2;
    protected final static int USER_TAB_INDEX = 0;
    protected final static int SOUNDFIT_TAB_INDEX = 1;

    protected FragmentManager mFragmentManager;
    protected Context mContext;

    public PlaylistPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case USER_TAB_INDEX:
                return mContext.getString(R.string.my_playlist);
            default:
            case SOUNDFIT_TAB_INDEX:
                return mContext.getString(R.string.soundfit_playlist);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        String tag;
        switch (position){
            case USER_TAB_INDEX:
                tag = PlaylistListFragment.TAG+USER_TAB_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = PlaylistListFragment.newInstance(true);
                }
                break;
            default:
            case SOUNDFIT_TAB_INDEX:
                tag = PlaylistListFragment.TAG+SOUNDFIT_TAB_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = PlaylistListFragment.newInstance(false);
                }
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return TAB_NUMBER;
    }


}
