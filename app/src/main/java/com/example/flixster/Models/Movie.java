package com.example.flixster.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String backDropPath;
    String title;
    String poster;
    String summary;
    double rating;
    int movieId;
    List<String> genres;
    String releaseDate;

    //Empty constructor needed by the Parceler library
    public Movie () { }

    public Movie(JSONObject jsonobject, JSONArray genreList) throws JSONException {
        backDropPath = jsonobject.getString("backdrop_path");
        poster = jsonobject.getString("poster_path");
        title = jsonobject.getString("title");
        summary = jsonobject.getString("overview");
        rating = jsonobject.getDouble("vote_average");
        movieId = jsonobject.getInt("id");
        genres = getGenres(jsonobject, genreList);
        releaseDate = jsonobject.getString("release_date");
    }

    private static List<String> getGenres(JSONObject jsonobject, JSONArray genre_id_def){
        //Returns a list of genres for each individual movie
        List<String> movGenreList = new ArrayList<String>();
        try {
            JSONArray genre_ids = jsonobject.getJSONArray("genre_ids");
            for (int i = 0; i < genre_id_def.length(); i++){
                for (int j = 0; j < genre_ids.length(); j++) {
                    if (genre_id_def.getJSONObject(i).getInt("id") ==
                            genre_ids.getInt(j)) {
                        movGenreList.add(genre_id_def.getJSONObject(i).getString("name"));
                    }
                }
            }
            Log.i("MOVIEMODEL", "Res: " + movGenreList.toString());
        } catch (JSONException e) {
            Log.e("MovieModel", "Failed to fetch genre_id list from json");
        }
        return movGenreList;
    }

    public static List<Movie> getMovieList(JSONArray arr, JSONArray genreList) throws JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < arr.length(); i++){
            movies.add(new Movie(arr.getJSONObject(i), genreList));
        }
        return movies;
    }

    public String getTitle() {
        return title;
    }

    public String getBackDropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backDropPath);
    }


    public String getPoster() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", poster);
    }

    public String getSummary() {
        return summary;
    }

    public double getRating() {
        return rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getGenreString() {
        //returns a string of comma-separated genres
        StringBuilder sb = new StringBuilder("");
        String prefix = "";
        for (String s: genres) {
            sb.append(prefix);
            prefix = ", ";
            sb.append(s);
        }
        String result = sb.toString();
        sb.setLength(0);
        return result;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
