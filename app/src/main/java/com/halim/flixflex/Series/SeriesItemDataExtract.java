package com.halim.flixflex.Series;

import android.util.Log;

import com.halim.flixflex.ClassesUtils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeriesItemDataExtract {

    //Method to fetch our data using JSON
    public static ArrayList<SeriesItem> getDataSeries(JSONObject jsonObj) throws JSONException {
        ArrayList<SeriesItem> seriesItems = new ArrayList<>();
        JSONArray series = jsonObj.getJSONArray("results");

        for (int j = 0; j < series.length(); j++) {
            try {
                JSONObject a = series.getJSONObject(j);

                seriesItems.add(new SeriesItem(a.getInt("id"),
                        a.getString("name"),
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
        return seriesItems;
    }

}
