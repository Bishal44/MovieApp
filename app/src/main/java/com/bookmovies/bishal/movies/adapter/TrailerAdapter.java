package com.bookmovies.bishal.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmovies.bishal.movies.R;
import com.bookmovies.bishal.movies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTViewHolder> {

    private Context context;
    private List<Trailer>trailerList;
    public TrailerAdapter(Context context, List<Trailer> trailerList) {

        this.context=context;
        this.trailerList=trailerList;
    }

    @NonNull
    @Override
    public TrailerAdapter.MyTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailercard,parent,false);
        return new MyTViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.MyTViewHolder holder, int position) {

        holder.title.setText(trailerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class MyTViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumnail;
        public MyTViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.Ttitle);
            thumnail=itemView.findViewById(R.id.imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position==RecyclerView.NO_POSITION){
                        Trailer trailer=trailerList.get(position);
                        String videoid=trailerList.get(position).getKey();
                        Intent trailerintent=new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoid));
                        trailerintent.putExtra("VIDEO_ID",videoid);
                        context.startActivity(trailerintent);
                        Toast.makeText(context, "You cliked"+trailer.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
