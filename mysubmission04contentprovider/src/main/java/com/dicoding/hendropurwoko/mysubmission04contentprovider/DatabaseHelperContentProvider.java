package com.dicoding.hendropurwoko.mysubmission04contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hendro on 3/13/2016.
 */

public class DatabaseHelperContentProvider extends SQLiteOpenHelper  {
    public final static String DATABASE_NAME = "dbMovieContentProvider.db";
    private final static int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_TABLE = "create table " + MovieContractContentProvider.TABLE_NAME + " (_id INTEGER PRIMARY KEY, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.TITLE + " TEXT, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.RELEASE_DATE + " TEXT, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.OVERVIEW + " TEXT, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.POPULARITY + " TEXT, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.FAVORITE + " TEXT, " +
                                    MovieContractContentProvider.MovieColumnsContentProvider.POSTER + " TEXT )";

    public DatabaseHelperContentProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContractContentProvider.TABLE_NAME);
        onCreate(db);
    }
}