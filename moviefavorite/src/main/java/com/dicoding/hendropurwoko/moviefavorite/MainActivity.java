package com.dicoding.hendropurwoko.moviefavorite;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.dicoding.hendropurwoko.moviefavorite.MovieContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private CPAdapter cpAdapter;
    ProgressDialog progressDialog;
    RecyclerView rvCP;

    private final int LOAD_NOTES_ID = 110;
    private Cursor list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Favorite Movies");
        getSupportActionBar().setSubtitle("Content Provider");

        rvCP = (RecyclerView) findViewById(R.id.recycler_view_cp);

        new LoadDataContentProvider().execute();
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

            display();
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
        cpAdapter = new CPAdapter(MainActivity.this);
        rvCP.setLayoutManager(new LinearLayoutManager(this));
        rvCP.setAdapter(cpAdapter);
        cpAdapter.setList(list);
        rvCP.setAdapter(cpAdapter);
    }
    /*
    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_NOTES_ID, null, (android.support.v4.app.LoaderManager.LoaderCallbacks<Object>) this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cpAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cpAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) cpAdapter.getItem(position);

        id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieColumns._ID));
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI+"/"+id));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
     private class LoadDataContentProvider extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TestCPActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));//ambil resource string
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Cursor aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            display();
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
    */
}
