package fr.soundfit.android.ui.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

/**
 * Created by Donovan on 05/02/2015.
 */
public class GenericChildActivity extends GenericActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (shouldUpRecreateTask(this, upIntent)) {
//              if (NavUtils.shouldUpRecreateTask(this, upIntent)) { TODO Bug Android ?
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
