package com.dicoding.hendropurwoko.moviefavorite;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.ArrayList;

import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnClickListener{
    ProgressDialog progressDialog;
    Menu menu;
    public static boolean stFavorite;

    private Cursor list;
    private CPAdapter cpAdapter;
    RecyclerView rvCP;

    private final int LOAD_NOTES_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCP = (RecyclerView)findViewById(R.id.recycler_view_cp);
        getSupportActionBar().setSubtitle("Content Provider");

        stFavorite = true;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow
        new LoadDataContentProvider().execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

    }

    private class LoadDataContentProvider extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));//ambil resource string
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Cursor aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            }

        @Override
        protected Cursor doInBackground(Void... voids) {
            list = getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            return list;
        }
    }

    private void display(){
        //Log.d("Info ", String.valueOf(list.getCount()));

        //cpAdapter = new CPAdapter(getApplicationContext());
        //rvCP.setLayoutManager(new LinearLayoutManager(this));
        //cpAdapter.replaceAll(list);
        //rvCP.setAdapter(cpAdapter);
    }

}
