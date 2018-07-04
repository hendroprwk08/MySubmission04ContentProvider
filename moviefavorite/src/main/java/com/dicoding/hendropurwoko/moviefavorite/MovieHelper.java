package com.dicoding.hendropurwoko.moviefavorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class MovieHelper {
    private Context context;
    private DatabaseHelper dataBaseHelper;
    private ArrayList<MovieModel> movieModels;
    private CPAdapter cpAdapter;

    private SQLiteDatabase database;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
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

    public void insertTransaction(MovieModel movieModel) {

        String sql = "INSERT INTO " + MovieContract.TABLE_MOVIE + " (" +
                MovieContract.MovieColumns.TITLE + ", " +
                MovieContract.MovieColumns.RELEASE_DATE + ", " +
                MovieContract.MovieColumns.OVERVIEW + ", " +
                MovieContract.MovieColumns.POPULARITY + ", " +
                MovieContract.MovieColumns.POSTER + ") VALUES (?, ?, ?, ?, ?)";

        Log.d("Info ", movieModel.getTitle() + " " + movieModel.getFavorite());

        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, movieModel.getTitle());
        stmt.bindString(2, movieModel.getRelease_date());
        stmt.bindString(3, movieModel.getOverview());
        stmt.bindString(4, movieModel.getPopularity());
        stmt.bindString(5, movieModel.getPoster());
        stmt.execute();
        stmt.clearBindings();
    }

    public ArrayList<MovieModel> getData(){
        ArrayList<MovieModel> movieModels = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MovieContract.TABLE_MOVIE + " ORDER BY " + MovieContract.MovieColumns._ID + " DESC", null);
        cursor.moveToFirst();
        MovieModel movieModel;

        if (cursor.getCount()>0) {
            do {
                movieModel = new MovieModel();
                movieModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns.TITLE)));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns.RELEASE_DATE)));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns.OVERVIEW)));
                movieModel.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns.POPULARITY)));
                movieModel.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns.POSTER)));

                movieModels.add(movieModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return movieModels;
    }

    public int update(MovieModel movieModel){
        ContentValues args = new ContentValues();
        return database.update(MovieContract.TABLE_MOVIE, args, _ID + "= '" + movieModel.getId() + "'", null);
    }

    public int delete(int id){
        return database.delete(MovieContract.TABLE_MOVIE, _ID + " = '"+id+"'", null);
    }

    /* CONTENT PROVIDER */
    public Cursor queryProvider(){
        return database.query(MovieContract.TABLE_MOVIE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }

    public Cursor queryByIdProvider(String id){
        return database.query(MovieContract.TABLE_MOVIE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public long insertProvider(ContentValues values){
        return database.insert(MovieContract.TABLE_MOVIE,null,values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(MovieContract.TABLE_MOVIE,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(MovieContract.TABLE_MOVIE,_ID + " = ?", new String[]{id});
    }
}