package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import fr.soundfit.android.ui.adapter.PlaylistPagerAdapter;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 05/02/2015.
 */
public class PlaylistPagerFragment extends PagerFragment {

    public static PlaylistPagerFragment newInstance() {
        PlaylistPagerFragment fragment = new PlaylistPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PagerAdapter getAdapter() {
        return new PlaylistPagerAdapter(getChildFragmentManager(), getActivity());
    }
}
