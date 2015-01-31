package fr.soundfit.android.ui.provider;

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
    private static final int SONG_SORT_TYPE = 101;

    protected static SoundfitDatabase mDatabase;

    protected static UriMatcher buildUriMatcher(final String authority) {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "authority=[" + authority + "]");
        }
        matcher.addURI(authority, SoundfitContract.PATH_SONG_SORT, SONG_SORT);
        matcher.addURI(authority, SoundfitContract.PATH_SONG_SORT + SEPARATOR + NUMBER, SONG_SORT_TYPE);
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

            case SONG_SORT_TYPE: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(SoundfitContract.Tables.SONG_SORT);
                select.append('.');
                select.append(SoundfitContract.SongSortTable.ID_TYPE);
                select.append(" = ");
                select.append(SoundfitContract.SongSortTable.getType(uri));
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
            case SONG_SORT_TYPE:
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
            case SONG_SORT_TYPE: {
                db.insertOrThrow(SoundfitContract.Tables.SONG_SORT, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SoundfitContract.SongSortTable.buildUriWithOwnId(values.getAsString(SoundfitContract.NewsTable._ID));
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

            case NEWS: {
                final int retVal = db.delete(EclypsiaContract.Tables.NEWS, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            case NEWS_ID: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(EclypsiaContract.Tables.NEWS);
                select.append('.');
                select.append(EclypsiaContract.NewsTable.ID);
                select.append(" = ");
                select.append(EclypsiaContract.NewsTable.getId(uri));
                selection = select.toString();
                final int retVal = db.delete(EclypsiaContract.Tables.NEWS, selection, selectionArgs);
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

            case NEWS: {
                final int retVal = db.update(EclypsiaContract.Tables.NEWS, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return retVal;
            }

            case NEWS_ID: {
                final StringBuilder select = new StringBuilder();
                if (!TextUtils.isEmpty(selection)) {
                    select.append(selection);
                    select.append(" AND ");
                }
                select.append(EclypsiaContract.Tables.NEWS);
                select.append('.');
                select.append(EclypsiaContract.NewsTable.ID);
                select.append(" = ");
                select.append(EclypsiaContract.NewsTable.getId(uri));
                selection = select.toString();
                final int retVal = db.update(EclypsiaContract.Tables.NEWS, values, selection, selectionArgs);
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