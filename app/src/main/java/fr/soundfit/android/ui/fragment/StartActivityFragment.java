package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 04/02/2015.
 */
public class StartActivityFragment extends GenericFragment {


    private Spinner mLevelSpinner;
    private SpinnerAdapter mLevelAdapter;
    private Spinner mMusicSpinner;
    private SpinnerAdapter mMusicAdapter;

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
        mLevelAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.preference_level_array , android.R.layout.simple_spinner_item);
        mLevelSpinner.setAdapter(mLevelAdapter);
        mMusicSpinner = (Spinner) view.findViewById(R.id.start_activity_spinner_music);
        mLevelAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.preference_music_array , android.R.layout.simple_spinner_item);
        mMusicSpinner.setAdapter(mLevelAdapter);
    }


}

