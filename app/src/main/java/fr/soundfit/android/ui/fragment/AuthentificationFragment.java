package fr.soundfit.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.WelcomeActivity;
import fr.soundfit.android.ui.utils.PrefUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 08/01/2015.
 */
public class AuthentificationFragment extends GenericFragment implements View.OnClickListener {

    public static final String TAG = AuthentificationFragment.class.getSimpleName();

    protected Button mNextButton;

    public static AuthentificationFragment newInstance() {
        AuthentificationFragment fragment = new AuthentificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_authentification;
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
            PrefUtils.setUserConnected(getActivity(), true);
            ((WelcomeActivity)act).onNextPageClick();
        }
    }

}
