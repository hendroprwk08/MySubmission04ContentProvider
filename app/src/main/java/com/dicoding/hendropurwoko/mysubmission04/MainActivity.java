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

import static com.dicoding.hendropurwoko.mysubmission04.MovieContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView nView;
    MovieHelper movieHelper;
    public ArrayList<MovieModel> movieAdapterList = new ArrayList<>();
    MovieModel movieModel;
    public static MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    RecyclerView rvMovie;
    int st;
    String search;
    Menu menu;

    Cursor list;

    AppPreference appPreference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        rvMovie = (RecyclerView) findViewById(R.id.recycler_view);

        appPreference = new AppPreference(getApplicationContext());

        if(appPreference.getFirstRun()) {
            new LoadData().execute();
        }

        displayRecyclerView("",  false);
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
            //movieAdapter.notifyDataSetChanged();
            displayRecyclerView("",  false);
        }
        //Log.d("Info:", String.valueOf(requestCode) + resultCode);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE);
        search = sharedPreferences.getString("search_key", "");
        st = sharedPreferences.getInt("status_key", 0);

        Log.d("Info ", st + " " + search);
        //if (!search.equals("")){
            //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            //searchView.setIconified(false); //expand
            //searchView.setIconified(true); //collapse
        //}

        if (st == 0) {
            if (!search.equals("")) {
                displayRecyclerView(search, false);
            }else{
                displayRecyclerView("",  false);
            }
        } else if (st == 1) {
            displayRecyclerView("", true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Cari Data");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO: kode cari
                if (newText.equals("")){
                    displayRecyclerView("",  false);
                }else {
                    displayRecyclerView(newText, false);
                }

                //simpan di shared preferences
                sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE); //1
                editor = sharedPreferences.edit(); //2
                editor.putString("search_key", newText); //3
                editor.putInt("status_key", 0); //3
                editor.apply(); //4

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            st = 0;
            displayRecyclerView("", false);

            sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE); //1
            editor = sharedPreferences.edit(); //2
            editor.putInt("status_key", st); //3
            editor.putString("search_key", ""); //3
            //sharedPreferences.edit().remove("search_key").commit();
            editor.apply(); //4
        } else if (id == R.id.nav_favorite) {
            st = 1;

            //simpan di shared preferences
            sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE); //1
            editor = sharedPreferences.edit(); //2
            editor.putInt("status_key", st); //3
            editor.apply(); //4

            displayRecyclerView("", true);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + getString(R.string.share_description));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
        }

        //simpan status orientation
        sharedPreferences = getApplicationContext().getSharedPreferences("operation", Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        editor.putInt("status_key", st); //3
        editor.commit(); //4

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LoadData extends AsyncTask<Void, Void, Cursor> {

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

            displayRecyclerView("",  false);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            appPreference.setFirstRun(false);
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }
    }

    private ArrayList<MovieModel> loadJSON() {
        String API = "86b7abdb2cb37ac9c3c148021f6724e5";
        String URL = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ API + "&language=en-US";

        final ArrayList<MovieModel> movieModels = new ArrayList();
        SyncHttpClient client = new SyncHttpClient();

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
                        movieModel.setFavorite("0");
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

    public void displayRecyclerView(String search, Boolean favorite) {
        movieAdapter = new MovieAdapter(getApplicationContext());
        movieHelper = new MovieHelper(getApplicationContext());
        rvMovie.setLayoutManager(new LinearLayoutManager(this));

        movieHelper.open();

        if (search.equals("") && favorite == false){
            movieAdapterList = movieHelper.getData("", false);
        }else if(!search.equals("") && favorite == false){
            movieAdapterList = movieHelper.getData(search, false);
        }else if(search.equals("") && favorite == true) {
            movieAdapterList = movieHelper.getData("", true);
        }

        //if (movieAdapterList.size() == 0) Toast.makeText(getApplicationContext(), "Tak Ada Data", Toast.LENGTH_SHORT).show();

        movieHelper.close();
        movieAdapter.addItem(movieAdapterList);
        rvMovie.setAdapter(movieAdapter);
    }
}
