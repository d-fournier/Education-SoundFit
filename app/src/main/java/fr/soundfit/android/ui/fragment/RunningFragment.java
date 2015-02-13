package fr.soundfit.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.model.Track;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import fr.soundfit.android.R;
import fr.soundfit.android.service.PlayerService;
import fr.soundfit.android.utils.ConstUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 04/02/2015.
 */
public class RunningFragment extends GenericFragment implements View.OnClickListener {

    PlayerService mService;
    boolean mBound = false;

    private ImageButton mPlayPause;
    private ImageButton mPreviousBt;
    private ImageButton mNextBt;
    private TextView mTitleTV;
    private TextView mArtistTV;
    private ImageView mCoverIV;

    private BroadcastReceiver mMessageReceiver = new TrackListener();

    public static RunningFragment newInstance() {
        RunningFragment fragment = new RunningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(ConstUtils.BroadcastConst.EVENT_NEW_TRACK));
        Intent intent = new Intent(getActivity().getApplicationContext(), PlayerService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_running;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mPlayPause = (ImageButton) view.findViewById(R.id.song_play_pause);
        mPlayPause.setOnClickListener(this);
        mPreviousBt = (ImageButton) view.findViewById(R.id.song_skip_backward);
        mPreviousBt.setOnClickListener(this);
        mNextBt = (ImageButton) view.findViewById(R.id.song_skip_forward);
        mNextBt.setOnClickListener(this);
        mTitleTV = (TextView) view.findViewById(R.id.song_title);
        mArtistTV = (TextView) view.findViewById(R.id.song_artist);
        mCoverIV = (ImageView) view.findViewById(R.id.song_cover);
    }

    @Override
    public void onClick(View view) {
        if(mBound){
            if(view == mPlayPause){
                mService.togglePlayer();
            } else if(view == mPreviousBt){
                mService.previousTrack();
            } else if(view == mNextBt){
                mService.nextTrack();
            }
        }
    }

    private void updateView(Track track){
        if(track != null){
            mTitleTV.setText(track.getTitle());
            mArtistTV.setText(track.getArtist().getName());
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.song_cover)
                    .build();
            // TODO Put in resources
            ImageLoader.getInstance().displayImage(track.getAlbum().getCoverUrl()+"?size=big", mCoverIV, defaultOptions);
        }
    }

     private class TrackListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Track t = intent.getParcelableExtra(ConstUtils.BroadcastConst.EXTRA_TRACK);
            updateView(t);
        }

    };

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            updateView(mService.getCurrentTrack());
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
