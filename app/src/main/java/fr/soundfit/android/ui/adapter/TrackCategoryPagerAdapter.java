package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deezer.sdk.model.Playlist;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.PlaylistListFragment;
import fr.soundfit.android.ui.fragment.TrackListFragment;

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
    protected Playlist mPlaylist;

    public TrackCategoryPagerAdapter(FragmentManager fm, Context context, Playlist playlist) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
        mPlaylist = playlist;
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
                tag = TrackListFragment.TAG+ SLOW_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = TrackListFragment.newInstance(mPlaylist, 0);
                }
                break;
            case MOVE_INDEX:
                tag = TrackListFragment.TAG+ MOVE_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = TrackListFragment.newInstance(mPlaylist, 2);
                }
                break;
            case NOT_SORTED_INDEX:
                tag = TrackListFragment.TAG+ NOT_SORTED_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = TrackListFragment.newInstance(mPlaylist, -1);
                }
                break;
            default:
            case NORMAL_INDEX:
                tag = TrackListFragment.TAG+ NORMAL_INDEX;
                frag = mFragmentManager.findFragmentByTag(tag);
                if(frag == null){
                    frag = TrackListFragment.newInstance(mPlaylist, 1);
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
