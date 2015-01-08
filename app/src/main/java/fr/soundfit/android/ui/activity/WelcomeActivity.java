package fr.soundfit.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.adapter.WelcomePagerAdapter;
import fr.soundfit.android.ui.utils.IntentUtils;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.activity
 * By Donovan on 07/01/2015.
 */
public class WelcomeActivity extends GenericActivity {

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


}
