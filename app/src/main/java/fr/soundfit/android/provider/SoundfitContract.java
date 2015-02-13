package fr.soundfit.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import fr.soundfit.android.BuildConfig;

/**
 * Created by Donovan on 31/01/2015.
 */
public class SoundfitContract {
    private static final String TAG = SoundfitContract.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static String CONTENT_AUTHORITY = "fr.soundfit.android.provider";
    protected static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    protected static String VENDOR_NAME = "vnd.soundfit.android";

    protected static final String PATH_SONG_SORT = "song_sort";

    public interface Tables {
        static final String SONG_SORT = "song_sort";
        static final String USER_DATA = "user_data";
    }


    /**
     * Song Sort
     */
    public interface SongSortColumns {
        /** Default Column _ID */
        /**
         * Primary key id (INTEGER)
         */
        static final String ID_SONG = "id_song";
        /**
         * Title (TEXT)
         */
        static final String ID_TYPE = "id_type";

    }
    public static class SongSortTable implements SongSortColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG_SORT).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + VENDOR_NAME + "." + PATH_SONG_SORT;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + VENDOR_NAME + "." + PATH_SONG_SORT;

        public static class PROJ {
            public static int _ID = 0;
            public static int ID_SONG = 1;
            public static int ID_TYPE = 2;

            public static String[] COLS = new String[]{ //
                    BaseColumns._ID,
                    Tables.SONG_SORT + "." + SongSortColumns.ID_SONG, //
                    Tables.SONG_SORT + "." + SongSortColumns.ID_TYPE, //
            };
        }

        public static Uri buildUri() {
            return CONTENT_URI;
        }


        public static Uri buildUriWithType(String type) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(type)).build();
        }

        public static String getSongID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}