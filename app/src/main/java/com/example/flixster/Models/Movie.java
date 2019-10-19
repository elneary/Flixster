package com.example.flixster.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    String backDropPath;
    String title;
    String poster;
    String summary;

    public Movie(JSONObject jsonobject) throws JSONException {
        backDropPath = jsonobject.getString("backdrop_path");
        poster = jsonobject.getString("poster_path");
        title = jsonobject.getString("title");
        summary = jsonobject.getString("overview");
    }

    public static List<Movie> getMovieList(JSONArray arr) throws JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < arr.length(); i++){
            movies.add(new Movie(arr.getJSONObject(i)));
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
}
