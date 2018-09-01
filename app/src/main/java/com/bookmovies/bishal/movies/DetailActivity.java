package com.bookmovies.bishal.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity{
    TextView nameofMovies,plotsynopsis,userRating,releaseDate;
    ImageView imageView;

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
            String rating=getIntent().getExtras().getString("vote_average");
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
}
