package com.dicoding.hendropurwoko.mysubmission04;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static com.dicoding.hendropurwoko.mysubmission04.MainActivity.*;

public class DetailActivity extends AppCompatActivity {
    TextView tvTitle, tvOverview, tvReleaseDate, tvPopularity;
    ImageView ivPoster;
    ImageButton ibFavorite;
    Bundle bundle;
    String id, title, overview, releaseDate, popularity, favorite, poster, stFavorite;
    public static int RESULT_CODE = 110;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        c = getApplicationContext();

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
                MovieHelper movieHelper = new MovieHelper(getApplicationContext());
                movieHelper.open();

                if (favorite.equals("false")){
                    ibFavorite.setImageResource(R.drawable.ic_favorite_black);

                    movieModel = new MovieModel();
                    movieModel.setTitle(title);
                    movieModel.setRelease_date(releaseDate);
                    movieModel.setOverview(overview);
                    movieModel.setPopularity(popularity);
                    movieModel.setPoster(poster);

                    movieHelper.beginTransaction();
                    movieHelper.insertTransaction(movieModel);
                    movieHelper.setTransactionSuccess();
                    movieHelper.endTransaction();

                    Toast.makeText(getApplicationContext(),"Favorite: "+ title, Toast.LENGTH_SHORT ).show();
                }else{
                    ibFavorite.setImageResource(R.drawable.ic_favorite_off_black);
                    movieHelper.delete(Integer.parseInt(id));
                    Toast.makeText(getApplicationContext(),"Dihapus: "+ title, Toast.LENGTH_SHORT ).show();
                }

                movieHelper.close();

                Intent i = new Intent();
                i.putExtra("Fav", String.valueOf(MainActivity.stFavorite));
                setResult(RESULT_CODE, i);
                finish();
            }
        });

        bundle = new Bundle();
        bundle = getIntent().getExtras();

        id = bundle.getString("id").toString();
        title = bundle.getString("title").toString();
        overview = bundle.getString("overview").toString();
        releaseDate = bundle.getString("release_date").toString();
        popularity = bundle.getString("popularity").toString();
        poster = bundle.getString("poster").toString();
        favorite = bundle.getString("favorite").toString();

        Log.d("Favorite: ", favorite);

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvReleaseDate.setText("RELEASE: "+releaseDate);
        tvPopularity.setText("RATE: "+popularity);

        Glide.with(c)
                .load(poster)
                .override(350, 350)
                .into(ivPoster);

        if (favorite.equals("false")){
            ibFavorite.setImageResource(R.drawable.ic_favorite_off_black);
        }else{
           ibFavorite.setImageResource(R.drawable.ic_favorite_black);
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
