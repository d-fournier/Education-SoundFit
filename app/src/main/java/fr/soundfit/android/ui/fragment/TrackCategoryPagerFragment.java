package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.GenericActivity;
import fr.soundfit.android.ui.adapter.TrackCategoryPagerAdapter;
import fr.soundfit.android.ui.view.SlidingTabLayout;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 05/02/2015.
 */
public class TrackCategoryPagerFragment extends GenericFragment implements SlidingTabLayout.TabColorizer {

    public static final String TAG = TrackCategoryPagerFragment.class.getSimpleName();

    private static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";

    private long mPlaylistId = 0;
    private Playlist mPlaylist;
    protected DeezerConnect mDeezerConnect = null;

    protected SlidingTabLayout mSlidingTabLayout;
    protected ViewPager mViewPager;


    public static TrackCategoryPagerFragment newInstance(long playlistId) {
        TrackCategoryPagerFragment fragment = new TrackCategoryPagerFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_PLAYLIST_ID, playlistId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        mDeezerConnect = ((GenericActivity)getActivity()).getDeezerConnection();
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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DeezerRequest request;
        request = DeezerRequestFactory.requestPlaylist(mPlaylistId);
        if(mPlaylist == null){
            AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);
        }
    }

    private class TrackListener extends JsonRequestListener {
        @Override
        public void onResult(Object result, Object requestId) {
            try {
                mPlaylist = (Playlist) result;
                mViewPager.setAdapter(new TrackCategoryPagerAdapter(getChildFragmentManager(), getActivity(), mPlaylist));
                mSlidingTabLayout.setDistributeEvenly(true);
                mSlidingTabLayout.setCustomTabColorizer(TrackCategoryPagerFragment.this);
                mSlidingTabLayout.setViewPager(mViewPager);
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
