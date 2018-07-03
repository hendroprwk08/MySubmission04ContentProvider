package com.dicoding.hendropurwoko.mysubmission04;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.dicoding.hendropurwoko.mysubmission04.MovieContract.CONTENT_URI;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private int REQUEST_CODE = 100;
    Context context;

    ArrayList<MovieModel> movieModels = new ArrayList<>();
    Context c ;

    private Cursor listMovies;

    public MovieAdapter(Context c) {this.c = c; }

    public void setListMovies(Cursor listMovies) {
        this.listMovies = listMovies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new ViewHolder(view);
    }

    private MovieModel getItem(int position){
        if (!listMovies.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieModel(listMovies);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, final int position) {
        /*
        Log.d ("Info ", movieModels.get(position).getTitle() + " " +
                movieModels.get(position).getRelease_date()+ " " +
                movieModels.get(position).getOverview()+ " " +
                movieModels.get(position).getPopularity()+ " " +
                movieModels.get(position).getPoster());
         */
        final MovieModel movieModel = getItem(position);
        holder.tvTitle.setText(movieModel.getTitle());
        holder.tvOverview.setText(movieModel.getOverview());
        holder.tvReleaseDate.setText(movieModel.getRelease_date());
        holder.tvFavorite.setText(movieModel.getFavorite());

        Glide.with(c)
                .load(movieModel.getPoster())
                .override(350, 350)
                .into(holder.ivPoster);

        holder.btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(movieModel.getId()));
                bundle.putString("title", movieModel.getTitle().toString().trim());
                bundle.putString("overview", movieModel.getOverview().toString().trim());
                bundle.putString("release_date", movieModel.getRelease_date().toString().trim());
                bundle.putString("popularity", movieModel.getPopularity().toString().trim());
                bundle.putString("poster", movieModel.getPoster().toString().trim());
                bundle.putString("favorite", movieModel.getFavorite().toString().trim());

                Intent detailIntent = new Intent(v.getContext(), DetailActivity.class);

                Uri uri = Uri.parse(CONTENT_URI+"/"+movieModel.getId());
                detailIntent.setData(uri);
                ((Activity) context).startActivityForResult(detailIntent, REQUEST_CODE);
            }
        });

        holder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, c.getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_SUBJECT, movieModel.getTitle().toString().trim());
                intent.putExtra(Intent.EXTRA_TEXT, movieModel.getTitle().toString().trim() + "\n\n" + movieModel.getOverview().toString().trim());
                v.getContext().startActivity(Intent.createChooser(intent, c.getResources().getString(R.string.share)));
            }
        });
    }

    public void addItem(ArrayList<MovieModel> movieList) {
        this.movieModels = movieList;
        notifyDataSetChanged();
    }
    public int getItemCount() {
        if (listMovies == null) return 0;
        return listMovies.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvOverview, tvReleaseDate, tvFavorite;
        ImageView ivPoster;
        Button btDetail, btShare;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title_now_playing);
            tvOverview = (TextView)itemView.findViewById(R.id.tv_overview_now_playing);
            tvReleaseDate = (TextView)itemView.findViewById(R.id.tv_release_date_now_playing);
            ivPoster = (ImageView)itemView.findViewById(R.id.iv_poster_now_playing);
            btDetail = (Button)itemView.findViewById(R.id.bt_detail_now_playing);
            btShare = (Button)itemView.findViewById(R.id.bt_share_now_playing);
            tvFavorite = (TextView) itemView.findViewById(R.id.tv_favorite);

        }
    }
}