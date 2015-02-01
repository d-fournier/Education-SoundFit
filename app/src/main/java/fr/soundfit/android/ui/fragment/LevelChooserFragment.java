package fr.soundfit.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.WelcomeActivity;
import fr.soundfit.android.utils.PrefUtils;
import fr.soundfit.android.ui.view.VerticalChooser;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 08/01/2015.
 */
public class LevelChooserFragment extends GenericFragment implements View.OnClickListener{

    public static final String TAG = LevelChooserFragment.class.getSimpleName();

    protected Button mNextButton;
    protected Button mFirstChoiceBt;
    protected Button mSecondChoiceBt;
    protected Button mThirdChoiceBt;
    protected VerticalChooser mChooser;

    public static LevelChooserFragment newInstance() {
        LevelChooserFragment fragment = new LevelChooserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_level;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mNextButton = (Button) view.findViewById(R.id.level_chooser_validate);
        mNextButton.setOnClickListener(this);
        mFirstChoiceBt = (Button) view.findViewById(R.id.chooser_first_choice);
        mFirstChoiceBt.setOnClickListener(this);
        mSecondChoiceBt= (Button) view.findViewById(R.id.chooser_second_choice);
        mSecondChoiceBt.setOnClickListener(this);
        mThirdChoiceBt = (Button) view.findViewById(R.id.chooser_third_choice);
        mThirdChoiceBt.setOnClickListener(this);
        mChooser = (VerticalChooser) view.findViewById(R.id.level_chooser_chooser);
        mChooser.setProgress(PrefUtils.getUserLevel(getActivity()));
    }



    @Override
    public void onClick(View v) {
        if(v == mNextButton){
            Activity act = getActivity();
            if(act != null && act instanceof WelcomeActivity){
                PrefUtils.setUserLevel(getActivity(), mChooser.getProgress());
                ((WelcomeActivity)act).onNextPageClick();
            }
        } else if (v == mFirstChoiceBt) {
            mChooser.setProgress(0);
        } else if (v == mSecondChoiceBt) {
            mChooser.setProgress(1);
        } else if (v == mThirdChoiceBt){
            mChooser.setProgress(2);
        }
    }

}
