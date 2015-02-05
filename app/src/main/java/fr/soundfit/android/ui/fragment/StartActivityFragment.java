package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 04/02/2015.
 */
public class StartActivityFragment extends GenericFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private final static int SOUNDFIT_PLAYLIST = 0;
    private final static int USER_PLAYLIST = 1;
    private final static int CUSTOM_LEVEL = 4;


    private Spinner mLevelSpinner;
    private RangeBar mRangeBar;
    private Spinner mMusicSpinner;
    private Spinner mPlaylistSpinner;
    private ImageButton mValidateButton;

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
        mRangeBar = (RangeBar) view.findViewById(R.id.start_activity_range_bar);
        mMusicSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_music);
        mMusicSpinner.setOnItemSelectedListener(this);
        mPlaylistSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_playlist);
        mValidateButton = (ImageButton) view.findViewById(R.id.start_activity_validate);
        mValidateButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLevelSpinner){
            displayCustomRange(position == CUSTOM_LEVEL);
        } else  if(parent == mMusicSpinner){
            displayPlaylist(position == USER_PLAYLIST);
        }
    }

    private void displayCustomRange(boolean isCustom) {
        if(isCustom){
            mRangeBar.setEnabled(true);
        } else {
            mRangeBar.setEnabled(false);
        }
    }

    private void displayPlaylist(boolean isUserPlaylist){
        if(isUserPlaylist){
            //TODO Populate
        } else {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    @Override
    public void onClick(View v) {
        if(v == mValidateButton){
            // TODO Launch Service and change Fragment
        }
    }
}

