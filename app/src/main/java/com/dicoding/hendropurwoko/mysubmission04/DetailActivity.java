package com.dicoding.hendropurwoko.mysubmission04;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView tvTitle, tvOverview, tvReleaseDate, tvPopularity;
    ImageView ivPoster;
    ImageButton ibFavorite;
    Bundle bundle;
    String id, title, overview, releaseDate, popularity, favorite, poster;
    public static int RESULT_CODE = 110;
    MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Context c = getApplicationContext();

        tvTitle = (TextView)findViewById(R.id.tv_title_detail);
        tvOverview = (TextView)findViewById(R.id.tv_overview_detail);
        tvReleaseDate = (TextView)findViewById(R.id.tv_date_detail);
        tvPopularity = (TextView)findViewById(R.id.tv_popularity_detail);
        ivPoster = (ImageView)findViewById(R.id.iv_poster_detail);
        ibFavorite = (ImageButton)findViewById(R.id.ib_favorite);
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            MovieModel movieModel;

            @Override
            public void onClick(View v) {
                movieModel = new MovieModel();
                MovieHelper movieHelper = new MovieHelper(getApplicationContext());

                if (favorite.equals("1")){
                    favorite = "0";

                    movieHelper.open();
                    movieModel.setId(Integer.parseInt(id.toString()));
                    movieModel.setFavorite(favorite);
                    movieHelper.update(movieModel);
                    movieHelper.close();

                    ibFavorite.setImageResource(R.drawable.ic_favorite_off_black);
                }else{
                    favorite = "1";

                    movieHelper.open();
                    movieModel.setId(Integer.parseInt(id.toString()));
                    movieModel.setFavorite(favorite);
                    movieHelper.update(movieModel);
                    movieHelper.close();

                    ibFavorite.setImageResource(R.drawable.ic_favorite_black);
                }

                Intent i = new Intent();
                setResult(RESULT_CODE, i);
                finish();
            }
        });

        /* CONTENT PROVIDER */
        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){
                if(cursor.moveToFirst()) movieModel = new MovieModel(cursor);
                cursor.close();
            }
        }
        /* ------------------------------- */

        if (movieModel != null){
            tvTitle.setText(movieModel.getTitle());
            tvOverview.setText(movieModel.getOverview());
            tvReleaseDate.setText(movieModel.getRelease_date());
            tvPopularity.setText("RATE: "+ movieModel.getPopularity());

            Glide.with(c)
                    .load(movieModel.getPoster())
                    .override(350, 350)
                    .into(ivPoster);

            favorite = movieModel.getFavorite();

            if (favorite.equals("0")){
                ibFavorite.setImageResource(R.drawable.ic_favorite_off_black);
            }else{
                ibFavorite.setImageResource(R.drawable.ic_favorite_black);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
