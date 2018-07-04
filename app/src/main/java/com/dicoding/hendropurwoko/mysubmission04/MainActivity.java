package com.dicoding.hendropurwoko.mysubmission04;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean stFavorite;

    NavigationView nView;
    MovieHelper movieHelper;
    ArrayList<MovieModel> movieModels = new ArrayList<>();
    MovieModel movieModel;
    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    RecyclerView rvMovie;
    Menu menu;
    Cursor list;
    String URL, API = "86b7abdb2cb37ac9c3c148021f6724e5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //defauit check navigation
        nView = (NavigationView) findViewById(R.id.nav_view);
        nView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setSubtitle("Home");

        rvMovie = (RecyclerView) findViewById(R.id.recycler_view);

        URL = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ API + "&language=en-US";
        new LoadMoviesData().execute();
        stFavorite = false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 110){
            boolean Fav = Boolean.parseBoolean(data.getStringExtra("Fav"));
            if (Fav == true) displayRecyclerView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Cari Data");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                nView.getMenu().getItem(0).setChecked(true);
                stFavorite = false;

                URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API + "&language=en-US&query=" + query;
                new LoadMoviesData().execute();
                displayJSONRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_content_provider){
            startActivity(new Intent(MainActivity.this, TestCPActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            stFavorite = false;
            getSupportActionBar().setSubtitle("Home");

            URL = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ API + "&language=en-US";
            new LoadMoviesData().execute();
            displayJSONRecyclerView();

            //sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE); //1
            //editor = sharedPreferences.edit(); //2
            //editor.putString("search_key", ""); //3
            //editor.apply(); //4
        } else if (id == R.id.nav_favorite) {
            getSupportActionBar().setSubtitle("Favorite");
            stFavorite = true;
            displayRecyclerView();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + getString(R.string.share_description));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LoadFavoriteMovies extends AsyncTask<Void, Void, ArrayList<MovieModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));//ambil resource string
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            //displayRecyclerView();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(Void... voids) {
            movieHelper = new MovieHelper(getApplicationContext());
            movieHelper.open();
            movieModels = movieHelper.getData();
            movieHelper.close();
            return movieModels;
        }
    }

    public void displayRecyclerView() {
        movieModels = new ArrayList<>();

        movieAdapter = new MovieAdapter(getApplicationContext());
        rvMovie.setLayoutManager(new LinearLayoutManager(this));

        //ambil nilai dari async
        try {
            movieModels = new LoadFavoriteMovies().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        movieAdapter.addItem(movieModels);
        rvMovie.setAdapter(movieAdapter);
    }

    /* LOAD JSON */
    private class LoadMoviesData extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));//ambil resource string
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            displayJSONRecyclerView();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(Void... voids) {
            movieModels = new ArrayList();
            SyncHttpClient client = new SyncHttpClient();

            Log.d("Info ", URL);

            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray jsonArray = jsonObj.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length() ; i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            movieModel = new MovieModel();

                            movieModel.setTitle(data.getString("title").toString().trim());
                            movieModel.setOverview(data.getString("overview").toString().trim());
                            movieModel.setRelease_date(data.getString("release_date").toString().trim());
                            movieModel.setPopularity(data.getString("popularity").toString().trim());
                            movieModel.setPoster("http://image.tmdb.org/t/p/w185"+data.getString("poster_path").toString().trim());

                            movieModels.add(movieModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("ERROR: ", statusCode +": "+ error.toString());
                }
            });
            return movieModels;
        }
    }

    private void displayJSONRecyclerView() {
        movieAdapter = new MovieAdapter(getApplicationContext());
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter.addItem(movieModels);
        rvMovie.setAdapter(movieAdapter);
    }
}