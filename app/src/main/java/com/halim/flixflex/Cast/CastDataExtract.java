package com.halim.flixflex.Cast;

import com.halim.flixflex.ClassesUtils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CastDataExtract {
    //Method to fetch our data using JSON
    public static ArrayList<CastItem> getDataCast(JSONObject jsonObj) throws JSONException {
        ArrayList<CastItem> castItems = new ArrayList<>();
        JSONArray cast = jsonObj.getJSONArray("cast");

        for (int j = 0; j < cast.length(); j++) {
            try {
                JSONObject a = cast.getJSONObject(j);

                castItems.add(new CastItem(a.getInt("id"),
                        a.getString("name"),
                        URLs.BASE_URL_IMAGE + a.getString("profile_path")
                ));
            } catch (Exception e) {
            }
        }
        return castItems;
    }


}
