package fr.soundfit.android.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
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
import fr.soundfit.android.ui.activity.PlayerActivity;
import fr.soundfit.android.ui.activity.SplashscreenActivity;
import fr.soundfit.android.ui.adapter.PlaylistAdapter;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 02/02/2015.
 */
public class PlaylistFragment extends GenericFragment implements PlayerWrapperListener, OnPlayerProgressListener, AdapterView.OnItemClickListener {

    public static final String TAG = PlaylistFragment.class.getSimpleName();

    protected DeezerConnect mDeezerConnect = null;
    private ListView mPlaylistLV;
    private PlaylistAdapter mAdapter;
    private List<Playlist> mPlaylistList;
    private PlaylistPlayer mPlaylistPlayer;
    private PlayerActivity mPlayerActivity = new PlayerActivity();

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist_list_user;
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

        //build the player
        createPlayer();

    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view,
                            final int position, final long id) {
        Playlist playlist = mPlaylistList.get(position);
        mPlaylistPlayer.playPlaylist(playlist.getId());
    //    createNotification(playlist.getTitle());
                /*// Launch the Home activity
                Intent intent = new Intent(UserPlaylistListFragment.this.getActivity(), UserTracksActivity.class);
                startActivity(intent);*/
    }

    /**
     * Je crée ma notification
     */

    private void createNotification(String notification_title){
        final NotificationManager mNotification = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        final Intent launchNotifiactionIntent = new Intent(getActivity(), SplashscreenActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(),
                0, launchNotifiactionIntent,
                PendingIntent.FLAG_ONE_SHOT);


        Notification.Builder builder = new Notification.Builder(getActivity().getApplicationContext())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app_title)
                .setContentTitle(notification_title)
                .setContentText("Cliquez sur moi je suis une notification")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_action_play, "Play", PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
                        getActivity().getIntent(), 0, null));

        mNotification.notify(0, builder.build());

    }

    /**
     * Creates the PlaylistPlayer
     */
    private void createPlayer() {
        try {
            mPlaylistPlayer = new PlaylistPlayer(getActivity().getApplication(), mDeezerConnect,
                    new WifiAndMobileNetworkStateChecker());
            mPlaylistPlayer.addPlayerListener(this);
            mPlaylistPlayer.addOnPlayerProgressListener(this);
        }
        catch (OAuthException e) {
            //handleError(e);
        }
        catch (TooManyPlayersExceptions e) {
           // handleError(e);
        }
        catch (DeezerError e) {
          //  handleError(e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new PlaylistListener());
        task.execute(request);
    }

    @Override
    public void onPlayerProgress(long l) {
    }

    @Override
    public void onAllTracksEnded() {
    }

    @Override
    public void onPlayTrack(Track track) {

    }

    @Override
    public void onTrackEnded(Track track) {

    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }

    private class PlaylistListener extends JsonRequestListener{
        @Override
        public void onResult(Object result, Object requestId) {
            mPlaylistList.clear();
            try {
                mPlaylistList.addAll((List<Playlist>) result);
            }
            catch (ClassCastException e) {
                displayError(true);
            }
            mAdapter.notifyDataSetChanged();
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