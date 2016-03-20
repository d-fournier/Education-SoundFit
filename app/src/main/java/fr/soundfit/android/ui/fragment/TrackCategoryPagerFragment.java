package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.GenericActivity;
import fr.soundfit.android.ui.activity.PlaylistActivity;
import fr.soundfit.android.ui.adapter.TrackCategoryPagerAdapter;
import fr.soundfit.android.ui.view.SlidingTabLayout;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 05/02/2015.
 */
public class TrackCategoryPagerFragment extends GenericFragment implements SlidingTabLayout.TabColorizer, View.OnClickListener {

    public static final String TAG = TrackCategoryPagerFragment.class.getSimpleName();

    private static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";
    private static final String EXTRA_IS_USER_PLAYLIST = "fr.soundfit.android.EXTRA_IS_USER_PLAYLIST";

    private long mPlaylistId = 0;
    private boolean mIsUserPlaylist;

    private Playlist mPlaylist;
    protected DeezerConnect mDeezerConnect = null;

    protected SlidingTabLayout mSlidingTabLayout;
    protected ViewPager mViewPager;
    protected FloatingActionButton mSortButton;


    public static TrackCategoryPagerFragment newInstance(long playlistId, boolean isUserPlaylist) {
        TrackCategoryPagerFragment fragment = new TrackCategoryPagerFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_PLAYLIST_ID, playlistId);
        args.putBoolean(EXTRA_IS_USER_PLAYLIST, isUserPlaylist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager_button;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(TrackCategoryPagerFragment.this);
        mSortButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mSortButton.setOnClickListener(this);
        mDeezerConnect = ((GenericActivity)getActivity()).getDeezerConnection();
        displayLoading(true);

        if(!mIsUserPlaylist){
            mViewPager.setAdapter(new TrackCategoryPagerAdapter(getChildFragmentManager(), getActivity(), mPlaylistId));
            mSlidingTabLayout.setViewPager(mViewPager);
            mSortButton.setVisibility(View.GONE);
            displayLoading(false);
        }
    }

    @Override
    public int getIndicatorColor(int position) {
        return getResources().getColor(R.color.theme_red);
    }

    @Override
    protected void initArg(Bundle args) {
        super.initArg(args);
        if(args != null){
            mPlaylistId = args.getLong(EXTRA_PLAYLIST_ID);
            mIsUserPlaylist = args.getBoolean(EXTRA_IS_USER_PLAYLIST, true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mPlaylist == null && mIsUserPlaylist){
            DeezerRequest request = DeezerRequestFactory.requestPlaylist(mPlaylistId);
            AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);
        }
    }

    @Override
    public void onClick(View view) {
        if(getActivity() instanceof PlaylistActivity){
            PlaylistActivity activity = (PlaylistActivity) getActivity();
            activity.displaySortFragment(mPlaylist);
        }
    }

    private class TrackListener extends JsonRequestListener {
        @Override
        public void onResult(Object result, Object requestId) {
            try {
                mPlaylist = (Playlist) result;
                mViewPager.setAdapter(new TrackCategoryPagerAdapter(getChildFragmentManager(), getActivity(), mPlaylist));
                mSlidingTabLayout.setViewPager(mViewPager);
                displayLoading(false);
            }
            catch (ClassCastException e) {
                displayError(true);
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) {
            displayError(true);
        }

        @Override
        public void onException(Exception exception, Object requestId) {
            displayError(true);
        }

    }

    private void displayError(boolean hasError){ }

}
