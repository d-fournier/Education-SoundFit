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
public class TrackCategoryPagerAdapter extends FragmentPagerAdapter {

    protected final static int TAB_NUMBER = 4;
    protected final static int SLOW_INDEX = 0;
    protected final static int NORMAL_INDEX = 1;
    protected final static int MOVE_INDEX = 2;
    protected final static int NOT_SORTED_INDEX = 3;

    protected FragmentManager mFragmentManager;
    protected Context mContext;

    public TrackCategoryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case SLOW_INDEX:
                return mContext.getString(R.string.slow_playlist);
            case MOVE_INDEX:
                return mContext.getString(R.string.move_playlist);
            case NOT_SORTED_INDEX:
                return mContext.getString(R.string.not_sorted_list);
            default:
            case NORMAL_INDEX:
                return mContext.getString(R.string.normal_playlist);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        String tag;
        switch (position){
            case SLOW_INDEX:
                tag = PlaylistListFragment.TAG+ SLOW_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = PlaylistListFragment.newInstance(true);
                }
                break;
            case MOVE_INDEX:
                tag = PlaylistListFragment.TAG+ MOVE_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = PlaylistListFragment.newInstance(false);
                }
                break;
            case NOT_SORTED_INDEX:
                tag = PlaylistListFragment.TAG+ NOT_SORTED_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = PlaylistListFragment.newInstance(true);
                }
                break;
            default:
            case NORMAL_INDEX:
                tag = PlaylistListFragment.TAG+ NORMAL_INDEX;
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
