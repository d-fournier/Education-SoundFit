package fr.soundfit.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import fr.soundfit.android.BuildConfig;

/**
 * Created by Donovan on 31/01/2015.
 */
public class SoundfitProvider extends ContentProvider {

    private static final String TAG = SoundfitProvider.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    protected static UriMatcher mUriMatcher;
    private static final String SEPARATOR = "/";
    private static final String NUMBER = "#";
    private static final String JOKER = "*";

    private static final int SONG_SORT = 100;
    private static final int SONG_SORT_ID = 101;

    protected static SoundfitDatabase mDatabase;

    protected static UriMatcher buildUriMatcher(final String authority) {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "authority=[" + authority + "]");
        }
        matcher.addURI(authority, SoundfitContract.PATH_SONG_SORT, SONG_SORT);
        matcher.addURI(authority, SoundfitContract.PATH_SONG_SORT + SEPARATOR + NUMBER, SONG_SORT_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate()");
        }
        mDatabase = new SoundfitDatabase(getContext(), SoundfitDatabase.DATABASE_NAME, null, SoundfitDatabase.DATABASE_VERSION);
        mUriMatcher = buildUriMatcher(SoundfitContract.CONTENT_AUTHORITY);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        }

        final SQLiteDatabase db = mDatabase.getReadableDatabase();
        if (db == null || !db.isOpen()) {
            return null;
        }

        final int match = mUriMatcher.match(uri);
        switch (match) {

            case SONG_SORT: {
                final Cursor c = db.query(SoundfitContract.Tables.SONG_SORT, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }

            case SONG_SORT_ID: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(SoundfitContract.Tables.SONG_SORT);
                select.append('.');
                select.append(SoundfitContract.SongSortTable.ID_SONG);
                select.append(" = ");
                select.append(SoundfitContract.SongSortTable.getSongID(uri));
                selection = select.toString();
                final Cursor c = db.query(SoundfitContract.Tables.SONG_SORT, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }

            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "match (" + match + ") is not handled in query");
                }
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case SONG_SORT:
                return SoundfitContract.SongSortTable.CONTENT_TYPE;
            case SONG_SORT_ID:
                return SoundfitContract.SongSortTable.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "insert(uri=" + uri + ", val=" + values + ")");
        }

        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        if (db == null || !db.isOpen()) {
            return null;
        }
        final int match = mUriMatcher.match(uri);
        switch (match) {

            case SONG_SORT:
            case SONG_SORT_ID: {
                db.insertOrThrow(SoundfitContract.Tables.SONG_SORT, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SoundfitContract.SongSortTable.buildUriWithSongID(values.getAsString(SoundfitContract.SongSortTable._ID));
            }

            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "match (" + match + ") is not handled in insert");
                }
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) {
            Log.d(TAG, "delete(uri=" + uri + ", sel=" + selection + ", selArg=" + Arrays.toString(selectionArgs) + ")");
        }

        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        if (db == null || !db.isOpen()) {
            return 0;
        }

        final int match = mUriMatcher.match(uri);

        switch (match) {

            case SONG_SORT: {
                final int retVal = db.delete(SoundfitContract.Tables.SONG_SORT, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            case SONG_SORT_ID: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(SoundfitContract.Tables.SONG_SORT);
                select.append('.');
                select.append(SoundfitContract.SongSortTable.ID_SONG);
                select.append(" = ");
                select.append(SoundfitContract.SongSortTable.getSongID(uri));
                selection = select.toString();
                final int retVal = db.delete(SoundfitContract.Tables.SONG_SORT, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "match (" + match + ") is not handled in delete");
                }
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "update(uri=" + uri + ", val=" + values + ", sel=" + selection + ", selArg=" + Arrays.toString(selectionArgs) + ")");
        }


        final SQLiteDatabase db = mDatabase.getReadableDatabase();
        if (db == null || !db.isOpen()) {
            return 0;
        }

        final int match = mUriMatcher.match(uri);
        switch (match) {

            case SONG_SORT: {
                final int retVal = db.update(SoundfitContract.Tables.SONG_SORT, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            case SONG_SORT_ID: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(SoundfitContract.Tables.SONG_SORT);
                select.append('.');
                select.append(SoundfitContract.SongSortTable.ID_SONG);
                select.append(" = ");
                select.append(SoundfitContract.SongSortTable.getSongID(uri));
                selection = select.toString();
                final int retVal = db.update(SoundfitContract.Tables.SONG_SORT, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "match (" + match + ") is not handled in Update");
                }
                return 0;
        }
    }
}