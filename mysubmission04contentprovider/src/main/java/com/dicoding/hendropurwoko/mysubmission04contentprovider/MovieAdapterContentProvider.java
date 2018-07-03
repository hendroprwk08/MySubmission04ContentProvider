package com.dicoding.hendropurwoko.mysubmission04contentprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapterContentProvider extends RecyclerView.Adapter<MovieAdapterContentProvider.ViewHolder> {
    private int REQUEST_CODE = 100;
    Context context;
    private Cursor list;

    ArrayList<MovieModelContentProvider> movieModels = new ArrayList<>();
    Context c ;

    public MovieAdapterContentProvider(Context c) {this.c = c; }

    @Override
    public MovieAdapterContentProvider.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterContentProvider.ViewHolder holder, final int position) {
        /*
        Log.d ("Info ", movieModels.get(position).getTitle() + " " +
                movieModels.get(position).getRelease_date()+ " " +
                movieModels.get(position).getOverview()+ " " +
                movieModels.get(position).getPopularity()+ " " +
                movieModels.get(position).getPoster());
         */

        holder.tvTitle.setText(movieModels.get(position).getTitle());
        holder.tvOverview.setText(movieModels.get(position).getOverview());
        holder.tvReleaseDate.setText(movieModels.get(position).getRelease_date());
        holder.tvFavorite.setText(movieModels.get(position).getFavorite());

        Glide.with(c)
                .load(movieModels.get(position).getPoster())
                .override(350, 350)
                .into(holder.ivPoster);

        holder.btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent detailIntent = new Intent(v.getContext(), DetailActivity.class);
                bundle.putString("id", String.valueOf(movieModels.get(position).getId()));
                bundle.putString("title", movieModels.get(position).getTitle().toString().trim());
                bundle.putString("overview", movieModels.get(position).getOverview().toString().trim());
                bundle.putString("release_date", movieModels.get(position).getRelease_date().toString().trim());
                bundle.putString("popularity", movieModels.get(position).getPopularity().toString().trim());
                bundle.putString("poster", movieModels.get(position).getPoster().toString().trim());
                bundle.putString("favorite", movieModels.get(position).getFavorite().toString().trim());

                //Log.d("Info ", movieModels.get(position).getFavorite().toString().trim());
                detailIntent.putExtras(bundle);
                //v.getContext().startActivity(detailIntent);
                //v.getContext().startActivityForResult(detailIntent, REQUEST_CODE);
                ((Activity) context).startActivityForResult(detailIntent, REQUEST_CODE);
            }
        });

        holder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, c.getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_SUBJECT, movieModels.get(position).getTitle().toString().trim());
                intent.putExtra(Intent.EXTRA_TEXT, movieModels.get(position).getTitle().toString().trim() + "\n\n" + movieModels.get(position).getOverview().toString().trim());
                v.getContext().startActivity(Intent.createChooser(intent, c.getResources().getString(R.string.share)));
            }
        });
    }

    public void addItem(ArrayList<MovieModelContentProvider> movieList) {
        this.movieModels = movieList;
        notifyDataSetChanged();
    }
    public int getItemCount() {
        return movieModels.size();
    }

    public void replaceAll(Cursor items) {
        list = items;
        notifyDataSetChanged();
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