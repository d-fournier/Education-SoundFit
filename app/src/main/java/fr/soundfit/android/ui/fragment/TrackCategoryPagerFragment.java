package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import fr.soundfit.android.ui.adapter.TrackCategoryPagerAdapter;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 05/02/2015.
 */
public class TrackCategoryPagerFragment extends PagerFragment {

    public static final String TAG = TrackCategoryPagerFragment.class.getSimpleName();

    public static TrackCategoryPagerFragment newInstance() {
        TrackCategoryPagerFragment fragment = new TrackCategoryPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PagerAdapter getAdapter() {
        return new TrackCategoryPagerAdapter(getChildFragmentManager(), getActivity());
    }
}
