package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.adapter.PlaylistPagerAdapter;
import fr.soundfit.android.ui.view.SlidingTabLayout;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 05/02/2015.
 */
public class PlaylistPagerFragment extends GenericFragment implements SlidingTabLayout.TabColorizer {

    protected SlidingTabLayout mSlidingTabLayout;
    protected ViewPager mViewPager;

    public static PlaylistPagerFragment newInstance() {
        PlaylistPagerFragment fragment = new PlaylistPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PlaylistPagerAdapter(getChildFragmentManager(), getActivity()));
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(this);
        mSlidingTabLayout.setViewPager(mViewPager);

        setHasOptionsMenu(true);
    }

    @Override
    public int getIndicatorColor(int position) {
        return getResources().getColor(R.color.theme_red);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                new MaterialDialog.Builder(getActivity()).title(R.string.tutorial_playlist_list_title).content(R.string.tutorial_playlist_list_desc)
                        .positiveText(R.string.ok).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
