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
public class MusicPreferenceChooserFragment extends GenericDialogFragment implements View.OnClickListener {

    public static final String TAG = MusicPreferenceChooserFragment.class.getSimpleName();

    private static final String EXTRA_IS_DIALOG = "fr.soundfit.android.EXTRA_IS_DIALOG";

    protected Button mNextButton;
    protected Button mFirstChoiceBt;
    protected Button mSecondChoiceBt;
    protected VerticalChooser mChooser;

    public static MusicPreferenceChooserFragment newInstance(boolean isDialog) {
        MusicPreferenceChooserFragment fragment = new MusicPreferenceChooserFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_DIALOG, isDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_preference;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mNextButton = (Button) view.findViewById(R.id.music_chooser_validate);
        mNextButton.setOnClickListener(this);
        mFirstChoiceBt = (Button) view.findViewById(R.id.chooser_first_choice);
        mFirstChoiceBt.setOnClickListener(this);
        mSecondChoiceBt= (Button) view.findViewById(R.id.chooser_second_choice);
        mSecondChoiceBt.setOnClickListener(this);
        mChooser = (VerticalChooser) view.findViewById(R.id.music_chooser_chooser);
        mChooser.setProgress(PrefUtils.getUserMusicPreference(getActivity()));
        if(mIsDialog) {
            mNextButton.setText(R.string.validate);
            view.findViewById(R.id.music_chooser_logo).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mNextButton){
            Activity act = getActivity();
            if(act != null && act instanceof WelcomeActivity){
                PrefUtils.setUserMusicPreference(getActivity(), mChooser.getProgress());
                ((WelcomeActivity)act).onNextPageClick();
            } else if(mIsDialog){
                PrefUtils.setUserMusicPreference(getActivity(), mChooser.getProgress());
                this.dismiss();
            }
        } else if (v == mFirstChoiceBt) {
            mChooser.setProgress(0);
        } else if (v == mSecondChoiceBt) {
            mChooser.setProgress(1);
        }
    }
}
