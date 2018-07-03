package com.dicoding.hendropurwoko.mysubmission04contentprovider;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import static com.dicoding.hendropurwoko.mysubmission04contentprovider.MovieContractContentProvider.MovieColumnsContentProvider.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMovieContentProvider;
    MovieAdapterContentProvider movieAdapter;
    private Cursor list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovieContentProvider = (RecyclerView) findViewById(R.id.recycler_view_content_provider);
        setupList();
        new loadDataAsync().execute();
    }

    private void setupList() {
        movieAdapter = new MovieAdapterContentProvider(this);
        rvMovieContentProvider.setLayoutManager(new LinearLayoutManager(this));
        rvMovieContentProvider.addItemDecoration(new DividerItemDecoration(rvMovieContentProvider.getContext(), DividerItemDecoration.VERTICAL));
        rvMovieContentProvider.setAdapter(movieAdapter);
    }

    private class loadDataAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Log.d("Info ", String.valueOf(CONTENT_URI));

            return getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            movieAdapter.replaceAll(list);
        }
    }
}

