package com.example.flixster.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.DetailActivity;
import com.example.flixster.Models.Movie;
import com.example.flixster.R;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
        Log.d("MovieAdapter", "onBindViewHolder" + position);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout movieContainer;
        TextView tvTitle;
        TextView tvOverview;
        ImageView moviePoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.movieTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieContainer = itemView.findViewById(R.id.movieContainer);

        }

        public void bind(final Movie movie)  {
            String imageURL;

            //use different images for portrait/landscape orientations
            if (context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                //orientation is landscape
                imageURL = movie.getBackDropPath();
            } else {
                //orientation is portrait
                imageURL = movie.getPoster();
            }
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getSummary());
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(context)
                    .load(imageURL)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(moviePoster);
            movieContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open new activity upon click
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }

    }

}
