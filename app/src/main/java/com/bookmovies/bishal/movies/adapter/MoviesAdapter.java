package com.bookmovies.bishal.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmovies.bishal.movies.DetailActivity;
import com.bookmovies.bishal.movies.R;
import com.bookmovies.bishal.movies.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private Context context;
    private List<Movie> movieList;

    public MoviesAdapter(Context context,List<Movie> movieList){
        this.context=context;
        this.movieList=movieList;

    }


    @NonNull
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MyViewHolder holder, int position) {

        holder.title.setText(movieList.get(position).getOriginalTitle());
        String vote=Double.toString(movieList.get(position).getVoteAverage());
        holder.userRating.setText(vote);

        Glide.with(context).load(movieList.get(position).getPosterPath())
                .apply(new RequestOptions().placeholder(R.drawable.ic_autorenew_black_24dp))
                .into(holder.thumbnial);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,userRating;
        public ImageView thumbnial;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            userRating=itemView.findViewById(R.id.userrating);
            thumbnial=itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position !=RecyclerView.NO_POSITION){
                        Movie clickedDataitem=movieList.get(position);
                        Intent intent=new Intent(context, DetailActivity.class);
                        intent.putExtra("originalTitle",movieList.get(position).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(position).getPosterPath());
                        intent.putExtra("overview",movieList.get(position).getOverview());
                        intent.putExtra("vote_average",movieList.get(position).getVoteAverage());
                        intent.putExtra("releaseDate",movieList.get(position).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);

                        Toast.makeText(view.getContext(), "you have cliked  "+clickedDataitem.getOriginalTitle(),Toast.LENGTH_LONG).show();

                        }
                }
            });
        }
    }
}
