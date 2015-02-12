package fr.soundfit.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;

import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.HomeActivity;

/**
 * Created by Donovan on 11/02/2015.
 */
public class PlayerService extends Service implements PlayerWrapperListener {

    private static final int NOTIFICATION_ID = 1502121158;
    public static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";


    private static boolean sRunning = false;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private TrackPlayer mTrackPlayer;
    private DeezerConnect mDeezerConnect;
    private long mPlaylistId;
    private Playlist mPlaylist;


    public class LocalBinder extends Binder {
        PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        buildNotification();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDeezerConnect = new DeezerConnect(this, getString(R.string.deezer_app_id));
        new SessionStore().restore(mDeezerConnect, this);
        try {
            mTrackPlayer = new TrackPlayer(getApplication(), mDeezerConnect, new WifiOnlyNetworkStateChecker());
            mTrackPlayer.addPlayerListener(this);
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
        buildNotification();
        sRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long playlistId = intent.getExtras().getLong(EXTRA_PLAYLIST_ID);
        play(playlistId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sRunning = false;
    }

    public static boolean isRunning(){
        return sRunning;
    }

    public void play(long playlistId){
        mPlaylistId = playlistId;
        DeezerRequest request = DeezerRequestFactory.requestPlaylist(mPlaylistId);
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
        task.execute(request);
    }

    private void buildNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_play)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, HomeActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }


    @Override
    public void onAllTracksEnded() {
        stopForeground(true);
    }

    @Override
    public void onPlayTrack(Track track) {

    }

    @Override
    public void onTrackEnded(Track track) {
        //TODO choose next song
    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }

    private class TrackListener extends JsonRequestListener {

        @Override
        public void onResult(Object result, Object requestId) {
            try {
                mPlaylist = (Playlist) result;
                mTrackPlayer.playTrack(mPlaylist.getTracks().get(0).getId());
            } catch (ClassCastException e) {
                // TODO
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) {      }

        @Override
        public void onException(Exception exception, Object requestId) {       }

    }


}
