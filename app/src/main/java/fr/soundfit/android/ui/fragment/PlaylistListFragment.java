package fr.soundfit.android.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.OAuthException;
import com.deezer.sdk.player.PlaylistPlayer;
import com.deezer.sdk.player.event.OnPlayerProgressListener;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.GenericActivity;
import fr.soundfit.android.ui.activity.HomeActivity;
import fr.soundfit.android.ui.activity.PlaylistActivity;
import fr.soundfit.android.ui.activity.SplashscreenActivity;
import fr.soundfit.android.ui.adapter.PlaylistAdapter;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 02/02/2015.
 */
public class PlaylistListFragment extends GenericFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = PlaylistListFragment.class.getSimpleName();

    private static final String EXTRA_USER_PLAYLIST = "fr.soundfit.android.EXTRA_USER_PLAYLIST";

    protected DeezerConnect mDeezerConnect = null;
    private boolean mIsUserPlaylist = false;
    private ListView mPlaylistLV;
    private PlaylistAdapter mAdapter;
    private List<Playlist> mPlaylistList;

    public static PlaylistListFragment newInstance(boolean isUserPlaylist) {
        PlaylistListFragment fragment = new PlaylistListFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_USER_PLAYLIST, isUserPlaylist);
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
        if(args != null){
            mIsUserPlaylist = args.getBoolean(EXTRA_USER_PLAYLIST, false);
        }
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mPlaylistLV = (ListView) view.findViewById(android.R.id.list);
        mPlaylistList = new ArrayList<Playlist>();
        mAdapter = new PlaylistAdapter(getActivity(), 0, mPlaylistList);
        mPlaylistLV.setAdapter(mAdapter);

        mDeezerConnect = ((GenericActivity)getActivity()).getDeezerConnection();

        mPlaylistLV.setOnItemClickListener(this);

        displayLoading(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        DeezerRequest request;
        if(mIsUserPlaylist){
            request = DeezerRequestFactory.requestCurrentUserPlaylists();
        } else {
            request = DeezerRequestFactory.requestUserPlaylists(getResources().getInteger(R.integer.soundfit_deezer_user_id));
        }
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new PlaylistListener());
        task.execute(request);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view,
                            final int position, final long id) {
        Playlist playlist = mPlaylistList.get(position);
        Intent i = new Intent(getActivity(), PlaylistActivity.class);
        i.putExtra(PlaylistActivity.EXTRA_PLAYLIST_ID, playlist.getId());
        startActivity(i);
    }

    private class PlaylistListener extends JsonRequestListener{
        @Override
        public void onResult(Object result, Object requestId) {
            mPlaylistList.clear();
            try {
                mPlaylistList.addAll((List<Playlist>) result);
                displayLoading(false);
                mAdapter.notifyDataSetChanged();
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

    private void displayError(boolean hasError){

    }

}