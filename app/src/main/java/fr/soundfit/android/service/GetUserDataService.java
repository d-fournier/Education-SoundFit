package fr.soundfit.android.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.sql.Timestamp;
import java.util.Random;

import java.util.ArrayList;

import fr.soundfit.android.provider.SoundfitContract;
import fr.soundfit.android.provider.SoundfitDatabase;
import fr.soundfit.android.utils.PrefUtils;

/**
 * Created by Emmael on 13/02/2015.
 */
public class GetUserDataService extends IntentService {

    public static final String TAG = GetUserDataService.class.getSimpleName();

    private final Random rand = new Random();
    private PebbleKit.PebbleDataReceiver sportsDataHandler = null;
    private int sportsState = Constants.SPORTS_STATE_INIT;
    private boolean useMetric = false;

    //Ma bdd
    protected static SoundfitDatabase mDatabase;
    //
    protected static PrefUtils mPrefUtils;

    public GetUserDataService() {
        super(TAG);
        //j'initialise ma base de donnée
        mDatabase = new SoundfitDatabase(getApplicationContext(), SoundfitDatabase.DATABASE_NAME, null, SoundfitDatabase.DATABASE_VERSION);
        //j'initialise ma variable
        //mPrefUtils = new PrefUtils();
    }
    public GetUserDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //création d'une content value
        ContentValues values = new ContentValues();
        //j'associe MES VALEURES à mes noms de colonnes
        values.put(SoundfitContract.UserDataColumns.SPEED,Constants.SPORTS_DATA_SPEED);
        values.put(SoundfitContract.UserDataColumns.DISTANCE,Constants.SPORTS_DISTANCE_KEY);
        values.put(SoundfitContract.UserDataColumns.TIMESTAMP,new Timestamp(System.currentTimeMillis()).getTime());

        /**
         * Il faut que je créer ma table USER DATA MAIS pas ici, dans SoundfitDatabase
         */

        //ouverture de la bdd en mode écriture
        final SQLiteDatabase db = mDatabase.getWritableDatabase();

        //Je recupere la vitesse et j'enregistre dans la bdd
        db.insertOrThrow(SoundfitContract.Tables.USER_DATA, null, values);

        // Send a broadcast to launch the specified application on the connected Pebble
        PebbleKit.startAppOnPebble(getApplicationContext(), Constants.SPORTS_UUID);


        //Choix de la musique
        ChoixDeLaMusique();

        //j'endore mon un peu mon service
        for (int i=0; i<5; i++) {
            Log.i("SimpleWakefulReceiver", "Running service " + (i + 1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
        Log.i("SimpleWakefulReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
        WakefulReceiver.completeWakefulIntent(intent);
    }

    /**
     * Ici je choisi la playliste en fonction du niveau de l'utilisateur et de sa vitesse
     *
     */
    public void ChoixDeLaMusique(){

        if (Constants.SPORTS_DATA_SPEED >6 && Constants.SPORTS_DATA_SPEED <9) {
            //ici je lance la playliste move
            PrefUtils.setNextSongType(getApplicationContext(),2);
        }
        else if (Constants.SPORTS_DATA_SPEED >9 && Constants.SPORTS_DATA_SPEED <11) {
            //ici je lance la playliste normal
            PrefUtils.setNextSongType(getApplicationContext(),1);

        }else if (Constants.SPORTS_DATA_SPEED >11 && Constants.SPORTS_DATA_SPEED <14) {
            //ici je lance la playliste slow
            PrefUtils.setNextSongType(getApplicationContext(),0);
        }
    }

    /**
     * Je calcul la moyenne des vitesse pour en deduire l'allure
     */

    public void CalculeDeAllure(){

    }
}