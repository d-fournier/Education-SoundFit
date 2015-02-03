package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 02/02/2015.
 */
public class UserPlaylistListFragment extends GenericFragment implements JsonRequestListener {

    private static final String REQUEST_USER_PLAYLIST = "fr.soundfit.android.REQUEST_USER_PLAYLIST";

    private ListView mPlaylistLV;
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
        new SessionStore().restore(mDeezerConnect, getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,this);
        task.execute(request);
    }

    @Override
    public void onResult(Object o, Object o2) {
        mPlaylistList.clear();
        try {
            mPlaylistList.addAll((List<Playlist>) result);
        }
        catch (ClassCastException e) {
            handleError(e);
        }
        mPlaylistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnparsedResult(String s, Object o) {
        handleError(new DeezerError("Unparsed reponse"));
    }

    @Override
    public void onException(Exception e, Object o) {
        handleError(exception);
    }
}
