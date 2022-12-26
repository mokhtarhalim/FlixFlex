package com.halim.flixflex.Videos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoDataExtract {
    //Method to fetch our data using JSON
    public static ArrayList<VideosItem> getDataVideo(JSONObject jsonObj) throws JSONException {
        ArrayList<VideosItem> videosItems = new ArrayList<>();
        JSONArray video = jsonObj.getJSONArray("results");

        for (int j = 0; j < video.length(); j++) {
            try {
                JSONObject a = video.getJSONObject(j);

                videosItems.add(new VideosItem(a.getString("id"),
                        a.getString("site"),
                        a.getString("key"),
                        a.getString("name"),
                        a.getString("type")
                ));
            } catch (Exception e) {
                Log.d("catch", "video: " + e);
            }
        }
        return videosItems;
    }

}
