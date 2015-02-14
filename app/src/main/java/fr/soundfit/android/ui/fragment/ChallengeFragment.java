package fr.soundfit.android.ui.fragment;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 03/01/2015.
 */

import android.os.Bundle;
import fr.soundfit.android.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChallengeFragment extends GenericFragment {

    public static ChallengeFragment newInstance() {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChallengeFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_challenge;
    }

}
