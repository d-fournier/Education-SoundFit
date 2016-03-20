package fr.soundfit.android.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import com.deezer.sdk.model.Playlist;

import java.util.ArrayList;

import fr.soundfit.android.provider.SoundfitContract;

/**
 * Created by Donovan on 11/02/2015.
 */
public class SortService extends IntentService {

    public static final String TAG = SortService.class.getSimpleName();

    public static final String EXTRA_SONG_ID = "fr.soundfit.android.EXTRA_SONG_ID";
    public static final String EXTRA_TYPE_ID = "fr.soundfit.android.EXTRA_TYPE_ID";



    public SortService() {
        super(TAG);
    }
    public SortService(String name) {
        super(name);
    }

    public static void launchService(Context ctx, long songId, int typeId){
        Intent intent = new Intent(ctx, SortService.class);
        Bundle extras = new Bundle();
        extras.putLong(EXTRA_SONG_ID, songId);
        extras.putInt(EXTRA_TYPE_ID, typeId);
        intent.putExtras(extras);
        ctx.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long songId = intent.getExtras().getLong(EXTRA_SONG_ID, -1);
        int typeId = intent.getExtras().getInt(EXTRA_TYPE_ID, -1);

        if(songId == -1 || typeId == -1){
            return;
        }

        ContentProviderOperation.Builder builder;
        Uri sortTypeUri = SoundfitContract.SongSortTable.buildUriWithType("" + 1);
        builder = ContentProviderOperation.newInsert(sortTypeUri);

        ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        batch.add(builder.withValue(SoundfitContract.SongSortTable.ID_SONG, songId)
                .withValue(SoundfitContract.SongSortTable.ID_TYPE, typeId)
                .build());
        try {
            getContentResolver().applyBatch(SoundfitContract.CONTENT_AUTHORITY, batch);
        } catch (RemoteException ex) {
            throw new RuntimeException("Error executing content provider batch operation", ex);
        } catch (OperationApplicationException ex) {
            throw new RuntimeException("Error executing content provider batch operation", ex);
        }


    }
}
