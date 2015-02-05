package fr.soundfit.android.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.sdk.model.Permissions;
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
import com.deezer.sdk.player.PlayerWrapper;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;


/**
 * Activity displaying a list of a user's most played tracks
 *
 * @author Deezer
 *
 */
public class UserTracksActivity extends GenericActivity{

    /** The list of tracks of displayed by this activity. */
    private List<Track> mTracksList = new ArrayList<Track>();

    // TODO TEST
    protected static final String[] PERMISSIONS = new String[] {
            Permissions.BASIC_ACCESS, Permissions.OFFLINE_ACCESS
    };

    /** the tracks list adapter */
    private ArrayAdapter<Track> mTracksAdapter;

    private TrackPlayer mTrackPlayer;

    private PlayerWrapper mPlayer;
    protected DeezerConnect mDeezerConnect = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Move code
        /*mDeezerConnect = new DeezerConnect(this, "151511");
        // TODO Leak
        mDeezerConnect.authorize(this, PERMISSIONS, mDeezerDialogListener);
        new SessionStore().restore(mDeezerConnect, this);*/

        // Setup the UI
        setContentView(R.layout.activity_tracklists);
        /*setupTracksList();
        setupPlayerUI();

        //build the player
        createPlayer();

        // fetch tracks list
        getUserTracks();*/
    }



}
