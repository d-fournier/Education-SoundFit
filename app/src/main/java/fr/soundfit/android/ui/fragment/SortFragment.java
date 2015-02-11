package fr.soundfit.android.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.soundfit.android.R;
import fr.soundfit.android.provider.SoundfitContract;
import fr.soundfit.android.service.SortService;
import fr.soundfit.android.ui.activity.PlaylistActivity;

/**
 * Created by Donovan on 10/02/2015.
 */
public class SortFragment extends GenericFragment implements  LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String TAG = SortFragment.class.getSimpleName();

    private static final int LOADER_TRACK_LIST = 1502091642;
    private static final String EXTRA_PLAYLIST = "fr.soundfit.android.EXTRA_PLAYLIST";

    private Playlist mPlaylist;
    private List<Track> mUnsortSong;
    private Track mDisplayedTrack;

    private TextView mSongNameTV;
    private ImageView mCoverSongIV;
    private Button mSlowBt;
    private Button mNormalBt;
    private Button mMoveBt;



    public static SortFragment newInstance(Playlist playlist) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PLAYLIST, playlist);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sort;
    }

    @Override
    protected void initArg(Bundle args) {
        super.initArg(args);
        if(args!=null){
            mPlaylist = args.getParcelable(EXTRA_PLAYLIST);
        }
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mUnsortSong = new ArrayList<>();
        mSongNameTV = (TextView) view.findViewById(R.id.sort_song_name);
        mCoverSongIV = (ImageView) view.findViewById(R.id.sort_cover);
        mSlowBt = (Button) view.findViewById(R.id.sort_slow_button);
        mSlowBt.setOnClickListener(this);
        mNormalBt = (Button) view.findViewById(R.id.sort_normal_button);
        mNormalBt.setOnClickListener(this);
        mMoveBt = (Button) view.findViewById(R.id.sort_move_button);
        mMoveBt.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().restartLoader(LOADER_TRACK_LIST, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_TRACK_LIST:
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        SoundfitContract.SongSortTable.buildUri(),        // Table to query
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

        mUnsortSong.clear();
        for(Track t : mPlaylist.getTracks()){
            if(!databaseContent.contains(t.getId())){
                mUnsortSong.add(t);
            }
        }
        displayNextSong();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUnsortSong.clear();
    }

    protected void displayNextSong(){
        if(mUnsortSong.size() == 0){
            if(getActivity() instanceof PlaylistActivity){
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        ((PlaylistActivity) getActivity()).hideSortFragment();
                    }
                };
                handler.sendEmptyMessage(0);
                return;
            } else {
                // TODO display no more songs
            }
        }
        mDisplayedTrack = mUnsortSong.get(0);
        ImageLoader.getInstance().displayImage(mDisplayedTrack.getAlbum().getCoverUrl(), mCoverSongIV);
        mSongNameTV.setText(mDisplayedTrack.getTitle() + " - " + mDisplayedTrack.getArtist().getName());
    }

    @Override
    public void onClick(View view) {
        if(view == mSlowBt || view == mNormalBt || view == mMoveBt){
            int type;
            if(view == mSlowBt){
                type = 0;
            } else if (view == mNormalBt){
                type = 1;
            } else {
                type = 2;
            }
            Toast.makeText(getActivity(), "Coucou", Toast.LENGTH_SHORT).show();
            SortService.launchService(getActivity(), mDisplayedTrack.getId(), type);
        }
        displayNextSong();
    }
}
