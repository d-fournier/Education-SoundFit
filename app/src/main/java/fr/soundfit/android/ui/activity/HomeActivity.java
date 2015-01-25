package fr.soundfit.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.fragment.NavigationDrawerFragment;
import fr.soundfit.android.ui.fragment.PlaceholderFragment;
import fr.soundfit.android.ui.utils.ResourceUtils;

public class HomeActivity extends GenericActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

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
    public void onNavigationDrawerItemSelected(String tag) {
        // update the main content by replacing fragments
        int nameId = ResourceUtils.getResourceId(ResourceUtils.ResourceType.STRING, tag, this);
        mTitle = getString(nameId);
        int id = ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, tag, this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        if(id == R.id.id_drawer_my_account){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_sharing){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_my_music){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_my_level){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_my_connected_objects){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_about_soundfit){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_discover_app){
            fragment = PlaceholderFragment.newInstance();
        } else if(id == R.id.id_drawer_contact_us){
            fragment = PlaceholderFragment.newInstance();
        } else {
            fragment = PlaceholderFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
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
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
