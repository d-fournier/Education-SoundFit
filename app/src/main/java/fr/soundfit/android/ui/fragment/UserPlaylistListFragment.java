package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.HomeActivity;
import fr.soundfit.android.ui.adapter.PlaylistAdapter;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 02/02/2015.
 */
public class UserPlaylistListFragment extends GenericFragment {

    public static final String TAG = UserPlaylistListFragment.class.getSimpleName();

    // TODO TEST
    protected static final String[] PERMISSIONS = new String[] {
            Permissions.BASIC_ACCESS, Permissions.OFFLINE_ACCESS
    };

    private ListView mPlaylistLV;
    private PlaylistAdapter mAdapter;
    private List<Playlist> mPlaylistList;
    protected DeezerConnect mDeezerConnect = null;

    public static UserPlaylistListFragment newInstance() {
        UserPlaylistListFragment fragment = new UserPlaylistListFragment();
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
        mPlaylistLV = (ListView) view;
        mPlaylistList = new ArrayList<Playlist>();
        mAdapter = new PlaylistAdapter(getActivity(), 0, mPlaylistList);
        mPlaylistLV.setAdapter(mAdapter);
        //TODO Move code
        mDeezerConnect = new DeezerConnect(getActivity(), "151511");
        // TODO Leak
        mDeezerConnect.authorize(this.getActivity(), PERMISSIONS, mDeezerDialogListener);
        new SessionStore().restore(mDeezerConnect, getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new PlaylistListener());
        task.execute(request);
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

    /**
     * A listener for the Deezer Login Dialog
     */
    private DialogListener mDeezerDialogListener = new DialogListener() {

        @Override
        public void onComplete(final Bundle values) {
            // store the current authentication info
            SessionStore sessionStore = new SessionStore();
            sessionStore.save(mDeezerConnect, UserPlaylistListFragment.this.getActivity());
        }

        @Override
        public void onException(final Exception exception) {
        }


        @Override
        public void onCancel() {
        }


    };
}
