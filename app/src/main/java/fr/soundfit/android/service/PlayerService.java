package fr.soundfit.android.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
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
import com.deezer.sdk.player.event.OnPlayerStateChangeListener;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.soundfit.android.R;
import fr.soundfit.android.provider.SoundfitContract;
import fr.soundfit.android.ui.activity.HomeActivity;
import fr.soundfit.android.utils.ConstUtils;
import fr.soundfit.android.utils.PrefUtils;
import fr.soundfit.android.utils.ResourceUtils;

/**
 * Created by Donovan on 11/02/2015.
 */
public class PlayerService extends Service implements PlayerWrapperListener, Loader.OnLoadCompleteListener<Cursor>, OnPlayerStateChangeListener {

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
    private int mCurrentType;

    private boolean mIsUserPlaylist;

    private CursorLoader mCursorLoader;
    private Cursor mCursor;
    private SparseArray<List<Track>> mAllTracks = new SparseArray<>();

    private SparseArray<List<Track>> mAvailableTracks = new SparseArray<>();

    private List<Long> mHistory = new ArrayList<>();

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        mIsBinded = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
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
            mTrackPlayer = new TrackPlayer(getApplication(), mDeezerConnect, new WifiAndMobileNetworkStateChecker());
            mTrackPlayer.addPlayerListener(this);
            mTrackPlayer.addOnPlayerStateChangeListener(this);
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getExtras() == null){
            terminateService();
        } else {
            sRunning = true;
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
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
        mCursor = null;
        if(mTrackPlayer != null){
            mTrackPlayer.stop();
            mTrackPlayer.release();
        }
        if(mAvailableTracks != null)
            mAvailableTracks.clear();
        if(mAllTracks != null)
            mAllTracks.clear();
        if(mHistory != null)
            mHistory.clear();
        mCurrentTrack = null;
    }

    private void updateNotification(){
        if(mCurrentTrack != null){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(mCurrentTrack.getTitle())
                            .setContentText(mCurrentTrack.getArtist().getName());
            Intent resultIntent = new Intent(this, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            startForeground(NOTIFICATION_ID, mBuilder.build());
        }
    }

    public void terminateService(){
        sRunning = false;
        mTrackPlayer.stop();
        stopForeground(true);
        stopSelf();
    }

    private void sendTrackBroadcast(){
        Intent intent = new Intent(ConstUtils.BroadcastConst.EVENT_NEW_TRACK);
        intent.putExtra(ConstUtils.BroadcastConst.EXTRA_TRACK, mCurrentTrack);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendEventBroadcast(String event, String message){
        Intent intent = new Intent(event);
        intent.putExtra(ConstUtils.BroadcastConst.EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onAllTracksEnded() {
        terminateService();
    }

    @Override
    public void onPlayTrack(Track track) {
        if(mCurrentTrack != null){
            mHistory.add(0, mCurrentTrack.getId());
        }
        mCurrentTrack = track;
        updateNotification();
        sendToConnectedThings();
        sendTrackBroadcast();
    }

    private void sendToConnectedThings() {
        //TODO Move elsewhere
        if(mCurrentTrack != null){
            final Intent i = new Intent("com.getpebble.action.NOW_PLAYING");
            i.putExtra("artist", mCurrentTrack.getArtist().getName());
            i.putExtra("album", mCurrentTrack.getAlbum().getTitle());
            i.putExtra("track", mCurrentTrack.getTitle());
            sendBroadcast(i);
        }
    }

    @Override
    public void onTrackEnded(Track track) {
        playNextTrack();
    }

    @Override
    public void onPlayerStateChange(PlayerState playerState, long l) {
        if(playerState.equals(PlayerState.PAUSED)){
            sendEventBroadcast(ConstUtils.BroadcastConst.EVENT_PAUSE, null);
        } else if(playerState.equals(PlayerState.PLAYING)){
            sendEventBroadcast(ConstUtils.BroadcastConst.EVENT_PLAY, null);
        } else if(playerState.equals(PlayerState.STOPPED)){
            sendEventBroadcast(ConstUtils.BroadcastConst.EVENT_STOP, null);
        }
    }

    private void playNextTrack(){
        int nextSongType = PrefUtils.getNextSongType(this);
        if(mAvailableTracks.get(nextSongType).size()==0){
            reloadPlaylist(nextSongType);
        }
        long id = mAvailableTracks.get(nextSongType).get(0).getId();
        mAvailableTracks.get(nextSongType).remove(0);
        mTrackPlayer.playTrack(id);
        mCurrentType = nextSongType;
    }

    private void startPlayer(){
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
            mAllTracks.put(0, new ArrayList<Track>());
            mAllTracks.put(1, new ArrayList<Track>());
            mAllTracks.put(2, new ArrayList<Track>());
            for(Track t : mPlaylist.getTracks()){
                Integer level = databaseContent.get(t.getId());
                if(level != null){
                    mAllTracks.get(level).add(t);
                }
            }
        }

        if(mAllTracks.size() != 3){
            return;
        }
        mAvailableTracks.put(0, new ArrayList<>(mAllTracks.get(0)));
        mAvailableTracks.put(1, new ArrayList<>(mAllTracks.get(1)));
        mAvailableTracks.put(2, new ArrayList<>(mAllTracks.get(2)));

        if(mAvailableTracks.get(0).size() == 0 || mAvailableTracks.get(1).size() == 0 || mAvailableTracks.get(2).size() == 0){
            sendEventBroadcast(ConstUtils.BroadcastConst.EVENT_ERROR, getString(R.string.error_playlist_not_completed));
            terminateService();
            return;
        }
        playNextTrack();
        sendEventBroadcast(ConstUtils.BroadcastConst.EVENT_INIT, null);
    }

    private void reloadPlaylist(int type){
        if(mAllTracks.size() != 3){
            return;
        }
        mAvailableTracks.put(type, new ArrayList<>(mAllTracks.get(type)));
    }

    @Override
    public void onRequestException(Exception e, Object o) {
        terminateService();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        if(mPlaylist != null){
            startPlayer();
        }
    }

    private class TrackListener extends JsonRequestListener {

        @Override
        public void onResult(Object result, Object requestId) {
            if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_ALL)){
                mPlaylist = (Playlist) result;
                if(mCursor != null){
                    startPlayer();
                }
            } else {
                if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_SLOW)){
                    mAllTracks.put(0, ((Playlist)result).getTracks());
                } else if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_MOVE)){
                    mAllTracks.put(2, ((Playlist)result).getTracks());
                } else if(((String)requestId).equalsIgnoreCase(DEEZER_PLAYLIST_NORMAL)){
                    mAllTracks.put(1, ((Playlist)result).getTracks());
                }
                if(mAllTracks.size() == 3){
                    startPlayer();
                }
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) {      }

        @Override
        public void onException(Exception exception, Object requestId) {       }
    }


    // ------ Bind Method
    public void nextTrack(){
        playNextTrack();
    }

    public boolean previousTrack(){
        if(mHistory.size()>0){
            mAvailableTracks.get(mCurrentType).add(0, mCurrentTrack);
            mCurrentTrack = null;
            mTrackPlayer.playTrack(mHistory.get(0));
            mHistory.remove(0);
            return true;
        }
        return false;
    }

    public boolean togglePlayer(){
        if(mTrackPlayer.getPlayerState().equals(PlayerState.PLAYING)){
            mTrackPlayer.pause();
            return false;
        } else {
            mTrackPlayer.play();
            return true;
        }
    }

    public Track getCurrentTrack(){
        return mCurrentTrack;
    }
}
