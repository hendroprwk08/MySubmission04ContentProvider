package com.dicoding.hendropurwoko.mysubmission04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import static com.dicoding.hendropurwoko.mysubmission04.MovieContract.CONTENT_URI;

public class TestCPActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView rvCP;
    Menu menu;

    private Cursor list;
    private CPAdapter cpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cp);

        rvCP = (RecyclerView)findViewById(R.id.recycler_view_cp);
        getSupportActionBar().setSubtitle("Content Provider");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow
        new LoadDataContentProvider().execute();
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

    private void display(){
        //Log.d("Info ", String.valueOf(list.getCount()));

        cpAdapter = new CPAdapter(getApplicationContext());
        rvCP.setLayoutManager(new LinearLayoutManager(this));
        cpAdapter.replaceAll(list);
        rvCP.setAdapter(cpAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 110){
            new LoadDataContentProvider().execute();
            display();
        }
    }
}
