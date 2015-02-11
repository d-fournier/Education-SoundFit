package fr.soundfit.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;

import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.SortFragment;
import fr.soundfit.android.ui.fragment.TrackCategoryPagerFragment;

/**
 * Created by Donovan on 05/02/2015.
 */
public class PlaylistActivity extends GenericChildActivity {

    public static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";

    private long mPlaylistId;
    private Fragment mMainFragment;
    private Fragment mSortFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mPlaylistId = extras.getLong(EXTRA_PLAYLIST_ID);
        }

        String tag = TrackCategoryPagerFragment.TAG + mPlaylistId;

        final FragmentManager fm = getSupportFragmentManager();
        mMainFragment = fm.findFragmentByTag(tag);
        if (mMainFragment == null) {
            mMainFragment =  TrackCategoryPagerFragment.newInstance(mPlaylistId);
            fm.beginTransaction().add(R.id.container, mMainFragment, tag).commit();
        } else {
            fm.beginTransaction().show(mMainFragment).commit();
        }
    }

    public void displaySortFragment(Playlist playlist){
        final FragmentManager fm = getSupportFragmentManager();
        String tag = SortFragment.TAG;
        mSortFragment = fm.findFragmentByTag(tag);
        if (mSortFragment == null) {
            mSortFragment =  SortFragment.newInstance(playlist);
            fm.beginTransaction().add(R.id.container, mSortFragment, tag).addToBackStack(tag).commit();
        } else {
            fm.beginTransaction().show(mSortFragment).addToBackStack(tag).commit();
        }
    }

    public void hideSortFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(mSortFragment);
        trans.commit();
        manager.popBackStack();
    }
}
