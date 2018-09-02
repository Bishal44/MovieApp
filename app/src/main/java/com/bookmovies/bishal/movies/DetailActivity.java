package com.bookmovies.bishal.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmovies.bishal.movies.adapter.TrailerAdapter;
import com.bookmovies.bishal.movies.api.Client;
import com.bookmovies.bishal.movies.api.Service;
import com.bookmovies.bishal.movies.model.Trailer;
import com.bookmovies.bishal.movies.model.TrailerResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity{
    TextView nameofMovies,plotsynopsis,userRating,releaseDate;
    ImageView imageView;
    private List<Trailer> trailerList;
    RecyclerView recyclerView;
    private TrailerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollaspingTollbar();


        imageView=findViewById(R.id.thumbnail_image_header);
        nameofMovies=findViewById(R.id.title);
        plotsynopsis=findViewById(R.id.plotsynopis);
        userRating=findViewById(R.id.userratingt);
        releaseDate=findViewById(R.id.releasedate);

        Intent intentthatStarted=getIntent();
        //Toast.makeText(this, "in"+intentthatStarted.getStringExtra("originalTitle"), Toast.LENGTH_SHORT).show();
        if (intentthatStarted.hasExtra("originalTitle")){

            String thumnail=getIntent().getExtras().getString("poster_path");
            String movieName=getIntent().getExtras().getString("originalTitle");
            String synopsis=getIntent().getExtras().getString("overview");
            String rating= String.valueOf(getIntent().getExtras().getDouble("vote_average"));
            String dateofRelease=getIntent().getExtras().getString("releaseDate");

            Glide.with(this)
                    .load(thumnail)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_autorenew_black_24dp))
                    .into(imageView);

            nameofMovies.setText(movieName);
            plotsynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateofRelease);
            }
            else {
            Toast.makeText(this, "No Api Data", Toast.LENGTH_LONG).show();
        }
        initviews();


    }

    private void initCollaspingTollbar() {
        final CollapsingToolbarLayout collapsingToolbar=findViewById(R.id.collasping_toolbar);
        collapsingToolbar.setTitle("");
        AppBarLayout appBarLayout=findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow=false;
            int scrollrange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollrange== -1){
                    scrollrange=appBarLayout.getTotalScrollRange();
                }
                if(scrollrange+verticalOffset  ==0){
                    collapsingToolbar.setTitle(getString(R.string.movie_details));
                    isShow=true;

                }else if(isShow){
                    collapsingToolbar.setTitle("");
                    isShow=false;

                }

            }
        });

    }
    //for trailer
    private void initviews(){
        trailerList= new ArrayList<>();
        adapter=new TrailerAdapter(this,trailerList);
        recyclerView=findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadjson();
    }

    private void loadjson() {
        int movie_id = getIntent().getExtras().getInt("id");

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(this, "please obtain your api key", Toast.LENGTH_SHORT).show();
            } else {
                Client client = new Client();
                Service apiService = Client.getClient().create(Service.class);
                Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
                call.enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        List<Trailer> trailer = response.body().getResults();
                        recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                        recyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {

                        Log.d("Error", t.getMessage());
                        Toast.makeText(DetailActivity.this, "Error in Fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (Exception e){

            Log.d("trailer error",e.getMessage());
            Toast.makeText(this, "error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
