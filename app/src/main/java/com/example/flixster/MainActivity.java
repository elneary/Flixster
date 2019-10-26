package com.example.flixster;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.Adapters.MovieAdapter;
import com.example.flixster.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String GENRES_URL = "https://api.themoviedb.org/3/genre/movie/list?language=en-US&api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";
    List<Movie> movies;
    JSONArray genres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncHttpClient client = new AsyncHttpClient();
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //Create an adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);


        //Set Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Fetch list of genre id and names
        client.get(GENRES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    genres = json.jsonObject.getJSONArray("genres");
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Genre results " + genres.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

        //Fetch list of movies and populate
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jobject = json.jsonObject;
                Log.d(TAG, "onSuccess");
                try {
                    JSONArray results = jobject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    movies.addAll(Movie.getMovieList(results, genres));
                    //let adapter know data changed
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}
