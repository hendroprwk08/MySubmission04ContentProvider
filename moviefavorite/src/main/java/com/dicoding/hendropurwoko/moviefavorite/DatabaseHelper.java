package com.dicoding.hendropurwoko.moviefavorite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hendro on 3/13/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper  {
    public final static String DATABASE_NAME = "dbMovie.db";
    private final static int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_TABLE = "create table " + MovieContract.TABLE_MOVIE + " (_id INTEGER PRIMARY KEY, " +
                                    MovieContract.MovieColumns.TITLE + " TEXT, " +
                                    MovieContract.MovieColumns.RELEASE_DATE + " TEXT, " +
                                    MovieContract.MovieColumns.OVERVIEW + " TEXT, " +
                                    MovieContract.MovieColumns.POPULARITY + " TEXT, " +
                                    MovieContract.MovieColumns.POSTER + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_MOVIE);
        onCreate(db);
    }
}