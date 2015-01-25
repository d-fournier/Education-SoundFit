package fr.soundfit.android.ui.fragment;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 03/01/2015.
 */

import android.app.Activity;
import android.os.Bundle;
import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.HomeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends GenericFragment {
    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

}
