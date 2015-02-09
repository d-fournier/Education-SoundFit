package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.adapter.PlaylistPagerAdapter;
import fr.soundfit.android.ui.view.SlidingTabLayout;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 01/02/2015.
 */
public abstract class PagerFragment extends GenericFragment implements SlidingTabLayout.TabColorizer {

    protected SlidingTabLayout mSlidingTabLayout;
    protected ViewPager mViewPager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(getAdapter());
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(this);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int getIndicatorColor(int position) {
        return getResources().getColor(R.color.theme_red);
    }

    protected abstract PagerAdapter getAdapter();

}
