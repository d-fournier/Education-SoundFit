package fr.soundfit.android.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.soundfit.android.R;
import fr.soundfit.android.service.PlayerService;
import fr.soundfit.android.ui.fragment.NavigationDrawerFragment;
import fr.soundfit.android.ui.fragment.ChallengeFragment;
import fr.soundfit.android.ui.fragment.PlaylistPagerFragment;
import fr.soundfit.android.ui.fragment.RunningFragment;
import fr.soundfit.android.ui.fragment.SettingsFragment;
import fr.soundfit.android.ui.fragment.StartActivityFragment;
import fr.soundfit.android.utils.ConstUtils;
import fr.soundfit.android.utils.ResourceUtils;

public class HomeActivity extends GenericActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private BroadcastReceiver mMessageReceiver = new PlayerListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ConstUtils.BroadcastConst.EVENT_INIT));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ConstUtils.BroadcastConst.EVENT_STOP));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ConstUtils.BroadcastConst.EVENT_ERROR));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onNavigationDrawerItemSelected(String tag) {
        // update the main content by replacing fragments
        int nameId = ResourceUtils.getResourceId(ResourceUtils.ResourceType.STRING, tag, this);
        mTitle = getString(nameId);
        int id = ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, tag, this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (id){
            case R.id.id_drawer_home:
                fragment = selectHomeFragment();
                break;
            case R.id.id_drawer_playlist:
                fragment = PlaylistPagerFragment.newInstance();
                break;
            case R.id.id_drawer_challenges:
                fragment = ChallengeFragment.newInstance();
                break;
            case R.id.id_drawer_parameter:
                fragment = SettingsFragment.newInstance();
                break;
            default:
                fragment = ChallengeFragment.newInstance();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private Fragment selectHomeFragment(){
        if(PlayerService.isRunning()){
            return RunningFragment.newInstance();
        } else {
            return StartActivityFragment.newInstance();

        }
    }

    private class PlayerListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConstUtils.BroadcastConst.EVENT_INIT) || intent.getAction().equals(ConstUtils.BroadcastConst.EVENT_STOP)) {
                mNavigationDrawerFragment.updateHomeFragment();
            } else if(intent.getAction().equals(ConstUtils.BroadcastConst.EVENT_ERROR)){
                Toast.makeText(HomeActivity.this, "Erreur", Toast.LENGTH_LONG).show();
                mNavigationDrawerFragment.updateHomeFragment();
            }

        }

    };
}
