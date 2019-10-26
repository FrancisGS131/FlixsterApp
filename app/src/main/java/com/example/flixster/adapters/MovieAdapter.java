package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter","onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false);
        //Wrapping this view inside a view holder
        return new ViewHolder(movieView);
    }

    //Involves populating data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("MovieAdapter","onBindViewHolder "+position);

        //Get the movie at the passed in position
        Movie movie = movies.get(position);

        //Bind the movie into the View Holder
        holder.bind(movie);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            //Some cases where we want to load the backdrop path!
            String imageURL;

            // If phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageURL = backdrop image
                imageURL = movie.getBackdropPath();
            }
            else {
                // else imageURL = poster image
                imageURL = movie.getPosterPath();
            }

            Glide.with(context).load(imageURL).into(ivPoster); // imageURL is either backdrop or poster path!

            /*
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            GlideApp.with(this)
                    .load("http://via.placeholder.com/300.png")
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivImg);
             */

            // 1. Register click listener on the whole row, not just the title!
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,movie.getTitle(),Toast.LENGTH_SHORT).show();
                    // 2. Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);

                    //i.putExtra("title",movie.getTitle());
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }
}
