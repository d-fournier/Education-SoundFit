package fr.soundfit.android.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseArray;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.soundfit.android.R;
import fr.soundfit.android.provider.SoundfitContract;
import fr.soundfit.android.ui.activity.HomeActivity;
import fr.soundfit.android.utils.ResourceUtils;

/**
 * Created by Donovan on 11/02/2015.
 */
public class PlayerService extends Service implements PlayerWrapperListener, Loader.OnLoadCompleteListener<Cursor> {

    private static final int NOTIFICATION_ID = 1502121158;
    private static final int LOADER_TRACK_LIST = 1502091642;
    private static final String DEEZER_PLAYLIST_MOVE = "fr.soundfit.android.DEEZER_PLAYLIST_MOVE";
    private static final String DEEZER_PLAYLIST_SLOW = "fr.soundfit.android.DEEZER_PLAYLIST_SLOW";
    private static final String DEEZER_PLAYLIST_NORMAL = "fr.soundfit.android.DEEZER_PLAYLIST_NORMAL";
    private static final String DEEZER_PLAYLIST_ALL = "fr.soundfit.android.DEEZER_PLAYLIST_ALL";

    public static final String EXTRA_PLAYLIST_ID = "fr.soundfit.android.EXTRA_PLAYLIST_ID";
    public static final String EXTRA_IS_USER_PLAYLIST = "fr.soundfit.android.EXTRA_IS_USER_PLAYLIST";


    private static boolean sRunning = false;

    private final IBinder mBinder = new LocalBinder();
    private boolean mIsBinded = false;

    private TrackPlayer mTrackPlayer;
    private DeezerConnect mDeezerConnect;
    private long mPlaylistId;
    private Playlist mPlaylist;
    private Track mCurrentTrack;

    private boolean mIsUserPlaylist;

    private CursorLoader mCursorLoader;
    private Cursor mCursor;
    private SparseArray<List<Track>> mAllTracks = new SparseArray<List<Track>>(3);

    private SparseArray<List<Track>> mAvailableTracks = new SparseArray<List<Track>>(3);

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
        mIsBinded = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        updateNotification();
        mIsBinded = false;
        return super.onUnbind(intent);
    }

    public static boolean isRunning(){
        return sRunning;
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
        sRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getExtras() == null){
            terminateService();
        } else {
            mPlaylistId = intent.getExtras().getLong(EXTRA_PLAYLIST_ID);
            mIsUserPlaylist = intent.getExtras().getBoolean(EXTRA_IS_USER_PLAYLIST);
        }
        if(mIsUserPlaylist){
            DeezerRequest request = DeezerRequestFactory.requestPlaylist(mPlaylistId);
            request.setId(DEEZER_PLAYLIST_ALL);
            AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);
            mCursorLoader = new CursorLoader(this, SoundfitContract.SongSortTable.buildUri(),
                    SoundfitContract.SongSortTable.PROJ.COLS, null, null, null);
            mCursorLoader.registerListener(LOADER_TRACK_LIST, this);
            mCursorLoader.startLoading();
        } else {
            int[] id = getResources().getIntArray(ResourceUtils.getResourceId(ResourceUtils.ResourceType.ARRAY,""+mPlaylistId, this));

            DeezerRequest request = DeezerRequestFactory.requestPlaylist(id[2]);
            request.setId(DEEZER_PLAYLIST_MOVE);
            AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);

            request = DeezerRequestFactory.requestPlaylist(id[0]);
            request.setId(DEEZER_PLAYLIST_SLOW);
            task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);

            request = DeezerRequestFactory.requestPlaylist(id[1]);
            request.setId(DEEZER_PLAYLIST_NORMAL);
            task = new AsyncDeezerTask(mDeezerConnect,new TrackListener());
            task.execute(request);

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sRunning = false;
        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }

    }

    private void updateNotification(){
        if(!mIsBinded && mCurrentTrack != null){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_action_play)
                            .setContentTitle(mCurrentTrack.getTitle())
                            .setContentText(mCurrentTrack.getArtist().getName());
            Intent resultIntent = new Intent(this, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            startForeground(NOTIFICATION_ID, mBuilder.build());
        } else {
            stopForeground(true);
        }
    }

    private void terminateService(){
        mTrackPlayer.stop();
        stopForeground(true);
        stopSelf();
    }


    @Override
    public void onAllTracksEnded() {
        terminateService();
    }

    @Override
    public void onPlayTrack(Track track) {
        mCurrentTrack = track;
        updateNotification();
    }

    @Override
    public void onTrackEnded(Track track) {
        if(!playNextTrack()){
            restartPlayer();
        }
    }

    private boolean playNextTrack(){
        if(mAvailableTracks.get(1).size()!=0){
            long id = mAvailableTracks.get(1).get(0).getId();
            mAvailableTracks.get(1).remove(0);
            mTrackPlayer.playTrack(id);
            return true;
        }
        return false;
    }

    private void restartPlayer(){
        if(mIsUserPlaylist){
            if(mCursor == null || mPlaylist == null){
                return;
            }
            Map<Long, Integer> databaseContent = new HashMap<>();
            if(mCursor!= null && mCursor.moveToFirst()) {
                databaseContent.put(mCursor.getLong(SoundfitContract.SongSortTable.PROJ.ID_SONG), mCursor.getInt(SoundfitContract.SongSortTable.PROJ.ID_TYPE));
                while (mCursor.moveToNext()){
                    databaseContent.put(mCursor.getLong(SoundfitContract.SongSortTable.PROJ.ID_SONG), mCursor.getInt(SoundfitContract.SongSortTable.PROJ.ID_TYPE));
                }
            }
            mAvailableTracks.put(0, new ArrayList<Track>());
            mAvailableTracks.put(1, new ArrayList<Track>());
            mAvailableTracks.put(2, new ArrayList<Track>());
            for(Track t : mPlaylist.getTracks()){
                mAvailableTracks.get(databaseContent.get(t.getId())).add(t);
            }
        } else {
            if(mAllTracks.size() != 3){
                return;
            }
            mAvailableTracks.put(0, new ArrayList<Track>(mAllTracks.get(0)));
            mAvailableTracks.put(1, new ArrayList<Track>(mAllTracks.get(1)));
            mAvailableTracks.put(2, new ArrayList<Track>(mAllTracks.get(2)));
        }
        playNextTrack();
        // TODO Case where No song in normal / slow / move mode
    }

    @Override
    public void onRequestException(Exception e, Object o) {
        terminateService();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        if(mPlaylist != null){
            restartPlayer();
        }
    }

    private class TrackListener extends JsonRequestListener {

        @Override
        public void onResult(Object result, Object requestId) {
            if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_ALL)){
                mPlaylist = (Playlist) result;
                if(mCursor != null){
                    restartPlayer();
                }
            } else if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_SLOW)){
                mAllTracks.put(0, ((Playlist)result).getTracks());
            } else if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_MOVE)){
                mAllTracks.put(2, ((Playlist)result).getTracks());
            } else if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_NORMAL)){
                mAllTracks.put(1, ((Playlist)result).getTracks());
            }
            if(mAllTracks.size() == 3){
                restartPlayer();
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) {      }

        @Override
        public void onException(Exception exception, Object requestId) {       }
    }


}
