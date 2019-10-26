package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyCTAeK8ZTJiOO4GaCbaJVvPts_4wo0qJcY";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    TextView movieTitle;
    TextView movieOverview;
    RatingBar ratingBar;
    YouTubePlayerView youtubePlayerView;
    TextView genreList;
    TextView releaseDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieTitle = findViewById(R.id.movieTitle);
        movieOverview = findViewById(R.id.movieOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youtubePlayerView = findViewById(R.id.player);
        genreList = findViewById(R.id.genreList);
        releaseDate = findViewById(R.id.releaseDate);

        final Movie movie  = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getSummary());
        ratingBar.setRating((float) movie.getRating());
        genreList.setText(movie.getGenreString());
        releaseDate.setText("In theaters " + movie.getReleaseDate());

        AsyncHttpClient client = new AsyncHttpClient();


        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length()==0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubeKey);
                    initializeYoutube(youtubeKey, movie);
                    Log.i("DetailActivity", "Rating: " + movie.getRating());
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }

    private void initializeYoutube(final String youtubeKey, final Movie movie){
        youtubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializedSuccess");
                if (movie.getRating() >= 7) {
                    //Autoplay movies with ratings greater than 7
                    youTubePlayer.loadVideo(youtubeKey);
                }
                else {
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializedFailure");
            }
        });
    }

}
