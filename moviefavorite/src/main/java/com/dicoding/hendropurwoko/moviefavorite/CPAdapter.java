package com.dicoding.hendropurwoko.moviefavorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.MovieColumns.OVERVIEW;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.MovieColumns.POPULARITY;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.MovieColumns.RELEASE_DATE;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.MovieColumns.TITLE;
import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.getColumnString;

public class CPAdapter extends CursorAdapter{
    public CPAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies_layout, parent, false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView tvTitle, tvOverview, tvReleaseDate;
            ImageView ivPoster;
            Button btDetail, btShare;

            tvTitle = (TextView)view.findViewById(R.id.tv_title_now_playing);
            tvOverview = (TextView)view.findViewById(R.id.tv_overview_now_playing);
            tvReleaseDate = (TextView)view.findViewById(R.id.tv_release_date_now_playing);
            ivPoster = (ImageView)view.findViewById(R.id.iv_poster_now_playing);
            btDetail = (Button)view.findViewById(R.id.bt_detail_now_playing);
            btShare = (Button)view.findViewById(R.id.bt_share_now_playing);

            tvTitle.setText(getColumnString(cursor,TITLE));
            tvOverview.setText(getColumnString(cursor,OVERVIEW));
            tvReleaseDate.setText(getColumnString(cursor,RELEASE_DATE));
        }
    }
}