package com.dicoding.hendropurwoko.moviefavorite;

import android.database.Cursor;

import static android.provider.BaseColumns._ID;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.getColumnInt;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.getColumnString;

public class MovieModel {
    int id;
    String title, overview, release_date, popularity, favorite, poster;

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieModel() {  }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public MovieModel(String title, String overview, String release_date, String popularity) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    /* CONTENT PROVIDER */
    public MovieModel(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, MovieContract.MovieColumns.TITLE);
        this.release_date = getColumnString(cursor, MovieContract.MovieColumns.RELEASE_DATE);
        this.overview = getColumnString(cursor, MovieContract.MovieColumns.OVERVIEW);
        this.popularity = getColumnString(cursor, MovieContract.MovieColumns.POPULARITY);
        this.poster = getColumnString(cursor, MovieContract.MovieColumns.POSTER);
    }
}
