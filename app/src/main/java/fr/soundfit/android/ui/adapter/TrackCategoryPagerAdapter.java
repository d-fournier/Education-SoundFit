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

    protected final static int TAB_NUMBER_USER_PLAYLIST = 4;
    protected final static int TAB_NUMBER_SOUNDFIT_PLAYLIST = 3;
    protected final static int SLOW_INDEX = 0;
    protected final static int NORMAL_INDEX = 1;
    protected final static int MOVE_INDEX = 2;
    protected final static int NOT_SORTED_INDEX = 3;

    protected FragmentManager mFragmentManager;
    protected Context mContext;

    protected Playlist mPlaylist;
    protected long mSoundfitPlaylistID;
    protected boolean mIsUserPlaylist;

    public TrackCategoryPagerAdapter(FragmentManager fm, Context context, Playlist playlist) {
        super(fm);
        mIsUserPlaylist = true;
        mFragmentManager = fm;
        mContext = context;
        mPlaylist = playlist;
    }

    public TrackCategoryPagerAdapter(FragmentManager fm, Context context, long playlistId) {
        super(fm);
        mIsUserPlaylist = false;
        mFragmentManager = fm;
        mContext = context;
        mSoundfitPlaylistID = playlistId;
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
        String tag = TrackListFragment.TAG + position;
        Fragment frag = mFragmentManager.findFragmentByTag(tag);
        if(frag == null){
            frag = mIsUserPlaylist ? TrackListFragment.newInstance(mPlaylist, position) : TrackListFragment.newInstance(mSoundfitPlaylistID, position);
        }
        return frag;
    }



    @Override
    public int getCount() {
        return mIsUserPlaylist ? TAB_NUMBER_USER_PLAYLIST : TAB_NUMBER_SOUNDFIT_PLAYLIST;
    }


}
