package com.dicoding.hendropurwoko.moviefavorite;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    static String TABLE_MOVIE = "table_movie";

    static final class MovieColumns implements BaseColumns {
        static String TITLE = "title";
        static String OVERVIEW = "overview";
        static String RELEASE_DATE = "release_date";
        static String POPULARITY = "popularity";
        static String POSTER = "poster";
    }

    public static final String AUTHORITY = "com.dicoding.hendropurwoko.mysubmission04";

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

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