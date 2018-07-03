package com.dicoding.hendropurwoko.mysubmission04contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContractContentProvider {
    static String TABLE_NAME = "table_movie";

    static final class MovieColumnsContentProvider implements BaseColumns {
        static String TITLE = "title";
        static String OVERVIEW  = "overview";
        static String RELEASE_DATE  = "release_date";
        static String POPULARITY  = "popularity";
        static String FAVORITE  = "favorite";
        static String POSTER = "poster";

        // Authority yang digunakan
        public static final String AUTHORITY = "com.dicoding.hendropurwoko.mysubmission04";

        // Base content yang digunakan untuk akses content provider
        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(AUTHORITY)
                .appendPath(MovieContractContentProvider.TABLE_NAME)
                .build();

        /*
        Digunakan untuk mempermudah akses data di dalam cursor dengan parameter nama column
        */
        public static String getColumnString(Cursor cursor, String columnName) {
            return cursor.getString( cursor.getColumnIndex(columnName) );
        }

        public static int getColumnInt(Cursor cursor, String columnName) {
            return cursor.getInt( cursor.getColumnIndex(columnName) );
        }

        public static long getColumnLong(Cursor cursor, String columnName) {
            return cursor.getLong( cursor.getColumnIndex(columnName) );
        }
    }
}
