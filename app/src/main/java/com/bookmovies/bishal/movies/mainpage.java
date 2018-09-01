package com.bookmovies.bishal.movies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bookmovies.bishal.movies.adapter.MoviesAdapter;
import com.bookmovies.bishal.movies.api.Client;
import com.bookmovies.bishal.movies.api.Service;
import com.bookmovies.bishal.movies.model.Movie;
import com.bookmovies.bishal.movies.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//implement for toprated
public class mainpage extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    public static final String Log_Tag=MoviesAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        initViews();

        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(mainpage.this, "Movie Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Activity getActivity(){

        Context context=this;
        while (context instanceof ContextWrapper){

            if(context instanceof Activity) {
                return (Activity) context;
            }
            context= ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        pd=new ProgressDialog(this);
        pd.setMessage("Fetching Movies......");
        pd.setCancelable(false);
        pd.show();


        recyclerView=findViewById(R.id.recycler_view);
        movieList=new ArrayList<>();
        adapter =new MoviesAdapter(this,movieList);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

      checkSortOrder();

    }

    private void LoadJson() {

        try {
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(this, "please obtain api key ", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Client client=new Client();
            Service apiService=Client.getClient().create(Service.class);
            Call<MovieResponse> call=apiService.getpopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> movies=response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.d("Error",t.getMessage());
                    Toast.makeText(mainpage.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (Exception e){
            Log.d("error",e.getMessage());
            Toast.makeText(this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();

        }

    }
    private void LoadJson1() {

        try {
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(this, "please obtain api key ", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Client client=new Client();
            Service apiService=Client.getClient().create(Service.class);
            Call<MovieResponse> call=apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> movies=response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.d("Error",t.getMessage());
                    Toast.makeText(mainpage.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (Exception e){
            Log.d("error",e.getMessage());
            Toast.makeText(this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_setting:
                Intent intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(Log_Tag,"Preferences updated");
        checkSortOrder();
    }

    private void checkSortOrder() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String sortorder=sharedPreferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if(sortorder.equals(this.getString(R.string.pref_most_popular))){
            Log.d(Log_Tag,"sorting by most popular");
            LoadJson();

        }else {
            Log.d(Log_Tag,"sorting by Average vote");
            LoadJson1();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(movieList.isEmpty()){
            checkSortOrder();
        }else {

        }
    }
}
