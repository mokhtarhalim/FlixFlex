package com.halim.flixflex.Movies;

import android.util.Log;

import com.halim.flixflex.ClassesUtils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Method to fetch our data using JSON
public class MoviesItemDataExtract {

    public static ArrayList<MoviesItem> getDataMovies(JSONObject jsonObj) throws JSONException {
        ArrayList<MoviesItem> moviesItems = new ArrayList<>();
        JSONArray movies = jsonObj.getJSONArray("results");

        for (int j = 0; j < movies.length(); j++) {
            try {
                JSONObject a = movies.getJSONObject(j);

                moviesItems.add(new MoviesItem(a.getInt("id"),
                        a.getString("title"),
                        a.getString("overview"),
                        URLs.BASE_URL_IMAGE + a.getString("backdrop_path"),
                        URLs.BASE_URL_IMAGE + a.getString("poster_path"),
                        a.getDouble("vote_average"),
                        a.getInt("vote_count")
                ));
            } catch (Exception e) {
                Log.d("catch", "catch: " + e);
            }
        }
        return moviesItems;
    }
}
