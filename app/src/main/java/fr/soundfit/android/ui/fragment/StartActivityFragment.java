package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

import fr.soundfit.android.R;

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
    private final static int MAX_BAR_VALUE = 10;
    private final static int TICK_RATIO = 10;



    private Spinner mLevelSpinner;
    private RangeBar mRangeBar;
    private TextView mRangeTV;
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
        mRangeBar.setOnRangeBarChangeListener(this);
        mRangeBar.setOnTouchListener(this);
        mRangeTV = (TextView) view.findViewById(R.id.start_activity_range_string);
        mMusicSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_music);
        mMusicSpinner.setOnItemSelectedListener(this);
        mPlaylistSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_playlist);
        mValidateButton = (ImageButton) view.findViewById(R.id.start_activity_validate);
        mValidateButton.setOnClickListener(this);
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

