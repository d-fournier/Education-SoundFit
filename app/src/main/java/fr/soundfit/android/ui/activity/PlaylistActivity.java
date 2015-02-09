package fr.soundfit.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.TrackCategoryPagerFragment;

/**
 * Created by Donovan on 05/02/2015.
 */
public class PlaylistActivity extends GenericChildActivity {

    public static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";

    private long mPlaylistId;
    private Fragment mFragment;

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
        mFragment = fm.findFragmentByTag(tag);
        if (mFragment == null) {
            mFragment =  TrackCategoryPagerFragment.newInstance();
            fm.beginTransaction().add(R.id.container, mFragment, tag).commit();
        } else {
            fm.beginTransaction().show(mFragment).commit();
        }
    }
}
