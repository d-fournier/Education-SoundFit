package fr.soundfit.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.WelcomeActivity;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 08/01/2015.
 */
public class LevelChooserFragment extends GenericFragment implements View.OnClickListener{

    public static final String TAG = LevelChooserFragment.class.getSimpleName();

    protected Button mNextButton;

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
        mNextButton = (Button) view.findViewById(R.id.welcome_next);
        mNextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Activity act = getActivity();
        if(act != null && act instanceof WelcomeActivity){
            ((WelcomeActivity)act).onNextPageClick();
        }
    }

}
