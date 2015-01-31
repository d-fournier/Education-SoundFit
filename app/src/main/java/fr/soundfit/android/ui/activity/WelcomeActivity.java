package fr.soundfit.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.adapter.WelcomePagerAdapter;
import fr.soundfit.android.utils.IntentUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.activity
 * By Donovan on 07/01/2015.
 */
public class WelcomeActivity extends GenericActivity {

    protected static final String STATE_PAGER_CURRENT_INDEX = "fr.soundfit.android.STATE_PAGER_CURRENT_INDEX";
    protected static final String STATE_PAGER_LAST_INDEX = "fr.soundfit.android.STATE_PAGER_LAST_INDEX";

    WelcomePagerAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.welcome_pager);
        mPager.setAdapter(mAdapter);
    }

    public void onNextPageClick(){
        if(mAdapter.getTotalPages()-1 == mPager.getCurrentItem()){
            IntentUtils.launchHomeActivity(this);
            finish();
        } else {
            mAdapter.enableNextPage();
            mPager.setCurrentItem(mPager.getCurrentItem()+1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null){
            outState.putInt(STATE_PAGER_CURRENT_INDEX, mPager.getCurrentItem());
            outState.putInt(STATE_PAGER_LAST_INDEX, mAdapter.getCurrentProgress());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            if(mPager != null && mAdapter != null){
                int index = savedInstanceState.getInt(STATE_PAGER_CURRENT_INDEX, 0);
                int max = savedInstanceState.getInt(STATE_PAGER_LAST_INDEX, 0);
                mAdapter.setCurrentProgress(max);
                mPager.setCurrentItem(index);
            }
        }
    }
}
