package fr.soundfit.android.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.soundfit.android.R;
import fr.soundfit.android.provider.SoundfitContract;
import fr.soundfit.android.ui.activity.GenericActivity;
import fr.soundfit.android.ui.adapter.TrackAdapter;
import fr.soundfit.android.utils.ResourceUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 09/02/2015.
 */
public class TrackListFragment extends GenericFragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = TrackListFragment.class.getSimpleName();

    private static final String EXTRA_IS_USER_PLAYLIST = "fr.soundfit.android.EXTRA_IS_USER_PLAYLIST";
    private static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";

    private static final String EXTRA_PLAYLIST = "fr.soundfit.android.EXTRA_PLAYLIST";
    private static final String EXTRA_TYPE = "fr.soundfit.android.EXTRA_TYPE";

    private static final String PLAYLIST_PREFIX = "playlist_";

    private static final int LOADER_TRACK_LIST = 1502091642;

    protected DeezerConnect mDeezerConnect = null;
    private ListView mTrackLV;
    private TrackAdapter mAdapter;
    private Playlist mPlaylist;
    private int mType;
    private long mPlaylistId;
    private boolean mIsUserPlaylist;
    private List<Track> mTrackList;


    public static TrackListFragment newInstance(Playlist playlist, int type) {
        TrackListFragment fragment = new TrackListFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_USER_PLAYLIST, true);
        args.putParcelable(EXTRA_PLAYLIST, playlist);
        args.putInt(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static TrackListFragment newInstance(long playlistId, int type) {
        TrackListFragment fragment = new TrackListFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_USER_PLAYLIST, false);
        args.putLong(EXTRA_PLAYLIST_ID, playlistId);
        args.putInt(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist_list_user;
    }

    @Override
    protected void initArg(Bundle args) {
        super.initArg(args);
        if(args!=null){
            mType = args.getInt(EXTRA_TYPE);
            if(args.getBoolean(EXTRA_IS_USER_PLAYLIST)){
                mIsUserPlaylist = true;
                mPlaylist = args.getParcelable(EXTRA_PLAYLIST);
            } else {
                mIsUserPlaylist = false;
                int resId = ResourceUtils.getResourceId(ResourceUtils.ResourceType.ARRAY, ""+args.getLong(EXTRA_PLAYLIST_ID), getActivity());
                mPlaylistId = getResources().getIntArray(resId)[mType];
            }
        }
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mTrackLV = (ListView) view.findViewById(android.R.id.list);
        mTrackList = new ArrayList<>();
        mAdapter = new TrackAdapter(getActivity(), 0, mTrackList);
        mTrackLV.setAdapter(mAdapter);
        mDeezerConnect = ((GenericActivity)getActivity()).getDeezerConnection();
        displayLoading(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mIsUserPlaylist){
            getLoaderManager().restartLoader(LOADER_TRACK_LIST, null, this);
        } else {
            DeezerRequest request = DeezerRequestFactory.requestPlaylist(mPlaylistId);
            AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);
        }
    }

    private class TrackListener extends JsonRequestListener {
        @Override
        public void onResult(Object result, Object requestId) {
            try {
                mPlaylist = (Playlist) result;
                mTrackList.clear();
                mTrackList.addAll(mPlaylist.getTracks());
                displayLoading(false);
                mAdapter.notifyDataSetChanged();
                displayLoading(false);
            }
            catch (ClassCastException e) {
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) {
        }

        @Override
        public void onException(Exception exception, Object requestId) {
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_TRACK_LIST:
                Uri uri;
                if(mType != -1){
                    uri = SoundfitContract.SongSortTable.buildUriWithType(""+mType);
                } else {
                    uri = SoundfitContract.SongSortTable.buildUri();
                }
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        uri,        // Table to query
                        SoundfitContract.SongSortTable.PROJ.COLS,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Sort order
                );
            default :
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Set<Long> databaseContent = new HashSet<>();
        if(data!= null && data.moveToFirst()) {
            databaseContent.add(data.getLong(SoundfitContract.SongSortTable.PROJ.ID_SONG));
            while (data.moveToNext()){
                databaseContent.add(data.getLong(SoundfitContract.SongSortTable.PROJ.ID_SONG));
            }
        }
        mTrackList.clear();
        if(mType != 3){
            for(Track t : mPlaylist.getTracks()){
                if(databaseContent.contains(t.getId())){
                    mTrackList.add(t);
                }
            }
        } else {
            for(Track t : mPlaylist.getTracks()){
                if(!databaseContent.contains(t.getId())){
                    mTrackList.add(t);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        displayLoading(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrackList.clear();
        mAdapter.notifyDataSetChanged();
    }
}
