package fr.soundfit.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.activity
 * By Donovan on 07/01/2015.
 */
public abstract class GenericActivity extends ActionBarActivity implements DialogListener {

    private final static String[] DEEZER_PERMISSIONS = new String[] {
            Permissions.BASIC_ACCESS, Permissions.OFFLINE_ACCESS
    };


    protected DeezerConnect mDeezerConnect = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeezerConnect = new DeezerConnect(this, getString(R.string.deezer_app_id));
        new SessionStore().restore(mDeezerConnect, this);

    }

    public DeezerConnect getDeezerConnection(){
        if(mDeezerConnect != null && !mDeezerConnect.isSessionValid()){
            mDeezerConnect.authorize(this, DEEZER_PERMISSIONS, this);
        }
        return mDeezerConnect;
    }

    @Override
    public void onComplete(final Bundle values) {
        // store the current authentication info
        SessionStore sessionStore = new SessionStore();
        sessionStore.save(mDeezerConnect, this);
    }

    @Override
    public void onException(final Exception exception) {
    }


    @Override
    public void onCancel() {
    }

}
