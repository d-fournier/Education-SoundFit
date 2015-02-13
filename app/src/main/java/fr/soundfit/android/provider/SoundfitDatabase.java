package fr.soundfit.android.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import fr.soundfit.android.BuildConfig;

/**
 * Created by Donovan on 31/01/2015.
 */
public class SoundfitDatabase extends SQLiteOpenHelper {

    private static final String TAG = SoundfitDatabase.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static String DATABASE_NAME = "soundfit_database";
    public static int DATABASE_VERSION = 1;

    public SoundfitDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        if (DEBUG) {
            Log.d(TAG, "SoundfitDatabase(context)");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createSongSortDatabase(sqLiteDatabase);
    }

    protected void createSongSortDatabase(SQLiteDatabase sqLiteDatabase) {
        String newsTable = new StringBuilder() //
                .append("CREATE TABLE ").append(SoundfitContract.Tables.SONG_SORT) //
                .append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
                .append(SoundfitContract.SongSortTable.ID_SONG).append("' INTEGER NOT NULL, '") //
                .append(SoundfitContract.SongSortTable.ID_TYPE).append("' INTEGER NOT NULL ); '") //
                .toString();
        execSQL(newsTable, sqLiteDatabase);
    }

    /**
     * Je crée la table UserData Dans lequel je vais enregistrer la vistesse et le timestamp
     * @param sqLiteDatabase
     */
    protected void createUserDataDatabase(SQLiteDatabase sqLiteDatabase) {
        String newsTable = new StringBuilder() //
                .append("CREATE TABLE ").append(SoundfitContract.Tables.USER_DATA) //
                .append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
                .append(SoundfitContract.UserDataTable.SPEED).append("' INTEGER NOT NULL, '") //
                .append(SoundfitContract.UserDataTable.TIMESTAMP).append("' INTEGER NOT NULL ); '") //
                .toString();
        execSQL(newsTable, sqLiteDatabase);
    }

    private void execSQL(String request, SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(request);
        if (DEBUG) {
            Log.d(TAG, request);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String deleteTable = new StringBuilder() //
                .append("DROP TABLE ") //
                .append(SoundfitContract.Tables.SONG_SORT).append(", ") //
                .toString();
        onCreate(sqLiteDatabase);
    }


}