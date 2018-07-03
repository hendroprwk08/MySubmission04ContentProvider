package com.dicoding.hendropurwoko.mysubmission04contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class MovieHelperContentProvider {
    private Context context;
    private DatabaseHelperContentProvider dataBaseHelper;
    private ArrayList<MovieModelContentProvider> movieModelContentProvider;
    private MovieAdapterContentProvider movieAdapterContentProvider;

    private SQLiteDatabase database;

    public MovieHelperContentProvider(Context context) {
        this.context = context;
    }

    public MovieHelperContentProvider open() throws SQLException {
        dataBaseHelper = new DatabaseHelperContentProvider(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public void insertTransaction(MovieModelContentProvider movieModel) {
        String sql = "INSERT INTO " + MovieContractContentProvider.TABLE_NAME + " (" +
                MovieContractContentProvider.MovieColumnsContentProvider.TITLE + ", " +
                MovieContractContentProvider.MovieColumnsContentProvider.RELEASE_DATE + ", " +
                MovieContractContentProvider.MovieColumnsContentProvider.OVERVIEW + ", " +
                MovieContractContentProvider.MovieColumnsContentProvider.POPULARITY + ", " +
                MovieContractContentProvider.MovieColumnsContentProvider.FAVORITE + ", " +
                MovieContractContentProvider.MovieColumnsContentProvider.POSTER + ") VALUES (?, ?, ?, ?, ?, ?)";

        Log.d("Info ", movieModel.getTitle() + " " + movieModel.getFavorite());

        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, movieModel.getTitle());
        stmt.bindString(2, movieModel.getRelease_date());
        stmt.bindString(3, movieModel.getOverview());
        stmt.bindString(4, movieModel.getPopularity());
        stmt.bindString(5, movieModel.getFavorite());
        stmt.bindString(6, movieModel.getPoster());
        stmt.execute();
        stmt.clearBindings();
    }

    public ArrayList<MovieModelContentProvider> getData(){
        ArrayList<MovieModelContentProvider> movieModels = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + MovieContractContentProvider.TABLE_NAME + " ORDER BY " + MovieContractContentProvider.MovieColumnsContentProvider._ID + " DESC", null);
        cursor.moveToFirst();

        MovieModelContentProvider movieModel;

        if (cursor.getCount()>0) {
            do {
                movieModel = new MovieModelContentProvider();
                movieModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.TITLE)));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.RELEASE_DATE)));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.OVERVIEW)));
                movieModel.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.POPULARITY)));
                movieModel.setFavorite(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.FAVORITE)));
                movieModel.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(MovieContractContentProvider.MovieColumnsContentProvider.POSTER)));

                movieModels.add(movieModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return movieModels;
    }


}