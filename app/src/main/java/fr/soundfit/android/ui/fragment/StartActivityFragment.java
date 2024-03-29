package fr.soundfit.android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.edmodo.rangebar.RangeBar;

import java.util.ArrayList;
import java.util.List;

import fr.soundfit.android.R;
import fr.soundfit.android.service.PlayerService;
import fr.soundfit.android.ui.activity.GenericActivity;
import fr.soundfit.android.utils.ConstUtils;
import fr.soundfit.android.utils.DeezerUtils;
import fr.soundfit.android.utils.PrefUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 04/02/2015.
 */
public class StartActivityFragment extends GenericFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, RangeBar.OnRangeBarChangeListener, View.OnTouchListener {


    private final static int SOUNDFIT_PLAYLIST = 0;
    private final static int USER_PLAYLIST = 1;
    private final static int CUSTOM_LEVEL = 3;

    private final static int MIN_BAR_VALUE = 5;
    private final static int TICK_RATIO = 10;

    protected DeezerConnect mDeezerConnect = null;
    private List<Playlist> mPlaylistList;
    private List<String> mPlaylistName;


    private Spinner mLevelSpinner;
    private RangeBar mRangeBar;
    private TextView mRangeTV;
    private Spinner mMusicSpinner;
    private Spinner mPlaylistSpinner;
    private ImageButton mValidateButton;

    private TextView mSpeedTV;
    private TextView mDistanceTV;

    public StartActivityFragment() {
    }

    public static StartActivityFragment newInstance() {
        StartActivityFragment fragment = new StartActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_start_activity;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mLevelSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_level);
        mLevelSpinner.setOnItemSelectedListener(this);
        mLevelSpinner.setSelection(PrefUtils.getUserLevel(getActivity()));
        mRangeBar = (RangeBar) view.findViewById(R.id.start_activity_range_bar);
        mRangeBar.setOnRangeBarChangeListener(this);
        mRangeBar.setOnTouchListener(this);
        mRangeTV = (TextView) view.findViewById(R.id.start_activity_range_string);
        mMusicSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_music);
        mMusicSpinner.setOnItemSelectedListener(this);
        mMusicSpinner.setSelection(PrefUtils.getUserMusicPreference(getActivity()));
        mValidateButton = (ImageButton) view.findViewById(R.id.start_activity_validate);
        mValidateButton.setOnClickListener(this);

        mDeezerConnect = ((GenericActivity)getActivity()).getDeezerConnection();
        mPlaylistSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_playlist);
        mPlaylistList = new ArrayList<Playlist>();
        mPlaylistName = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mPlaylistName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPlaylistSpinner.setAdapter(adapter);

        mSpeedTV = (TextView) view.findViewById(R.id.speed_text);
        mDistanceTV = (TextView) view.findViewById(R.id.distance_text);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                new MaterialDialog.Builder(getActivity()).title(R.string.tutorial_start_activity_title).content(R.string.tutorial_start_activity_desc)
                        .positiveText(R.string.ok).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO Launch Loader for last Informations
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLevelSpinner){
            displayCustomRange(position);
        } else  if(parent == mMusicSpinner){
            displayPlaylist(position == USER_PLAYLIST);
        }
    }

    private void displayCustomRange(int position) {
        if(position == CUSTOM_LEVEL){
            return;
        }
        int min =  getResources().getIntArray(R.array.level_min_array)[position];
        int max = getResources().getIntArray(R.array.level_max_array)[position];
        mRangeBar.setThumbIndices(min, max);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view == mRangeBar && motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            mLevelSpinner.setSelection(CUSTOM_LEVEL);
        }
        return false;
    }

    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int i, int i2) {
        float min = (float)i/TICK_RATIO + MIN_BAR_VALUE;
        float max = (float)i2/TICK_RATIO + MIN_BAR_VALUE;
        mRangeTV.setText(getResources().getString(R.string.level_range_string, min, max));
    }

    private void displayPlaylist(boolean isUserPlaylist){
        mValidateButton.setEnabled(false);
        DeezerRequest request;
        if(isUserPlaylist){
            request = DeezerRequestFactory.requestCurrentUserPlaylists();
        } else {
            request = DeezerRequestFactory.requestUserPlaylists(getResources().getInteger(R.integer.deezer_user_soundfit_id));
        }
        AsyncDeezerTask task = new AsyncDeezerTask(mDeezerConnect,new PlaylistListener());
        task.execute(request);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    @Override
    public void onClick(View v) {
        if(v == mValidateButton){
            if(isPebbleInstalled()){
                launchRun();
            } else {
                displayDialogPebble();
            }
        }
    }


    private class PlaylistListener extends JsonRequestListener {
        @Override
        public void onResult(Object result, Object requestId) {
            mPlaylistList.clear();
            mPlaylistName.clear();
            try {
                for (Playlist p : (List<Playlist>) result){
                    if(DeezerUtils.isPlaylistAvailable(p)){
                        mPlaylistList.add(p);
                        mPlaylistName.add(p.getTitle());
                    }
                }
            }
            catch (ClassCastException e) {
            }
            ((ArrayAdapter)mPlaylistSpinner.getAdapter()).notifyDataSetChanged();
            if(mPlaylistList.size()>0){
                mValidateButton.setEnabled(true);
            }
        }

        @Override
        public void onUnparsedResult(String response, Object requestId) { }

        @Override
        public void onException(Exception exception, Object requestId) { }

    }

    private boolean isPebbleInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(ConstUtils.PEBBLE_URI, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }

    private void displayDialogPebble() {
        new MaterialDialog.Builder(getActivity()).title(R.string.error_pebble_not_found_title).content(R.string.error_pebble_not_found_desc)
                .positiveText(R.string.ok).negativeText(R.string.test).negativeColorRes(R.color.theme_red).callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        launchRun();
                    }
                }).show();
    }

    private void launchRun(){
        mValidateButton.setEnabled(false);
        Intent intent = new Intent(getActivity(), PlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putLong(PlayerService.EXTRA_PLAYLIST_ID, mPlaylistList.get(mPlaylistSpinner.getSelectedItemPosition()).getId());
        bundle.putBoolean(PlayerService.EXTRA_IS_USER_PLAYLIST, mMusicSpinner.getSelectedItemPosition() == USER_PLAYLIST);
        intent.putExtras(bundle);
        getActivity().startService(intent);
        displayLoading(true);
    }


}

