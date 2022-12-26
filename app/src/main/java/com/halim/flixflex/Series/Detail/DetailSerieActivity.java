package com.halim.flixflex.Series.Detail;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.halim.flixflex.Cast.CastDataExtract;
import com.halim.flixflex.Cast.CastItem;
import com.halim.flixflex.Cast.CastRecyclerAdapter;
import com.halim.flixflex.ClassesUtils.StaticMethods;
import com.halim.flixflex.ClassesUtils.URLs;
import com.halim.flixflex.Videos.VideoDataExtract;
import com.halim.flixflex.Videos.VideosItem;
import com.halim.flixflex.R;
import com.halim.flixflex.Series.SeriesItem;
import com.halim.flixflex.Series.SeriesItemDataExtract;
import com.halim.flixflex.Series.SeriesRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class DetailSerieActivity extends AppCompatActivity {

    private MotionLayout motionLayout;
    private TextView title;
    private ImageView btnBack;
    private ImageView posterPicture;
    private TextView btnWatchTrailer;
    private TextView voteAverage;
    private TextView voteCount;
    private TextView releaseDate;
    private TextView runtime;
    private TextView genre;
    private TextView description;
    private LinearLayout castLayout;
    private LinearLayout similarSerieLayout;
    private RatingBar ratingBar;
    private RecyclerView castRecyclerview;
    private RecyclerView similarSeriesRecyclerview;

    private int idSerie = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_serie);

        //Get id of serie from Adapter to fetch all detail of the serie
        if (getIntent().getExtras() != null) {
            idSerie = getIntent().getExtras().getInt("idSerie");
        }

        findViewsById();

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

                //Changing max line of the title when using scrolls up and down
                if (motionLayout.getProgress() == 0.0) {
                    // this is start
                    title.setMaxLines(Integer.MAX_VALUE);
                    title.setEllipsize(null);
                } else {
                    // this is end
                    title.setEllipsize(TextUtils.TruncateAt.END);
                    title.setMaxLines(1);
                }

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
            }
        });

        btnBack.setOnClickListener(view -> onBackPressed());

        //Store the data of each movie with idMovie to reduce API call,
        //For example if user check for movie id=236, he backPress than re-enter
        //We use data stored in shared preferences instead of re-calling API with the same id , we gonna have same result
        //But each time user close the App, all shared preferences will be deleted
        sharedPreferences = getSharedPreferences(getString(R.string.flixflexsharedpref), MODE_PRIVATE);
        if (sharedPreferences.getString("serie" + idSerie, null) != null) {

            String strJson = sharedPreferences.getString("serie" + idSerie, null);//second parameter is necessary ie.,Value to return if this preference does not exist.

            if (strJson != null) {
                try {
                    //Display our data from shared preferences
                    JSONObject response = new JSONObject(strJson);
                    showData(response);
                } catch (JSONException e) {

                }
            }
        } else {
            //If this serie is not stored, we call the API with id of series
            getDetailSerie(idSerie);
        }

    }

    //Initializing our variables
    private void findViewsById() {
        motionLayout = findViewById(R.id.motionLayout);
        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        posterPicture = findViewById(R.id.posterPicture);
        btnWatchTrailer = findViewById(R.id.btnWatchTrailer);
        ratingBar = findViewById(R.id.ratingBar);
        voteAverage = findViewById(R.id.voteAverage);
        voteCount = findViewById(R.id.voteCount);
        releaseDate = findViewById(R.id.releaseDate);
        runtime = findViewById(R.id.runtime);
        genre = findViewById(R.id.genre);
        description = findViewById(R.id.description);
        castLayout = findViewById(R.id.castLayout);
        castRecyclerview = findViewById(R.id.castRecyclerview);
        similarSerieLayout = findViewById(R.id.similarSerieLayout);
        similarSeriesRecyclerview = findViewById(R.id.similarSeriesRecyclerview);
    }

    private void getDetailSerie(int id) {
        //Check for user's connection
        if (StaticMethods.isInternetAvailable()) {
            RequestQueue requestQueue = Volley.newRequestQueue(DetailSerieActivity.this);
            StringRequest postRequest = new StringRequest(Request.Method.GET, URLs.DETAIL_SERIE_URL(id), response -> {

                try {
                    //Get response from our API than storing it in Sharedpreferences, with a unique ID
                    JSONObject jsonObj = new JSONObject(response);

                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    prefsEditor.putString("serie" + idSerie, jsonObj.toString());
                    prefsEditor.apply();

                    //Display our data
                    showData(jsonObj);

                } catch (JSONException e) {
                    StaticMethods.snackBarError(DetailSerieActivity.this);
                }

            }, error -> {
                StaticMethods.snackBarError(DetailSerieActivity.this);
            });
            requestQueue.add(postRequest);
        } else {
            StaticMethods.snackBarNoInternet(DetailSerieActivity.this);
        }
    }

    //Method to display our data from JSON
    private void showData(JSONObject jsonObj) {
        try {

            //Display Poster using Glide Library
            Glide.with(DetailSerieActivity.this)
                    .asBitmap()
                    .load(URLs.BASE_URL_IMAGE + jsonObj.getString("backdrop_path"))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            posterPicture.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


            //Display other informations
            title.setText(jsonObj.getString("name"));
            ratingBar.setRating((float) ((jsonObj.getDouble("vote_average")) / 2));
            voteAverage.setText(String.valueOf(jsonObj.getDouble("vote_average")));
            voteCount.setText(String.valueOf(jsonObj.getInt("vote_count")));
            description.setText(jsonObj.getString("overview"));

            try {
                releaseDate.setText(StaticMethods.getDateText(jsonObj.getString("first_air_date")));
            } catch (ParseException e) {
                releaseDate.setText(jsonObj.getString("first_air_date"));
                throw new RuntimeException(e);
            }

            String inProduction = "";

            if (jsonObj.getBoolean("in_production")) {
                inProduction = "In production";
            } else {
                inProduction = "Finished";
            }

            runtime.setText(jsonObj.getInt("number_of_seasons") + " Season(s) " + getString(R.string.bullet) + " " + jsonObj.getInt("number_of_episodes") + " Episodes " + getString(R.string.bullet) + " " + inProduction);

            //Display genre of the serie
            String genreMovie = "";
            JSONArray res = jsonObj.getJSONArray("genres");
            for (int i = 0; i < res.length(); i++) {
                JSONObject c = res.getJSONObject(i);
                genreMovie += c.getString("name") + ", ";
            }

            if (genreMovie.length() > 2) {
                genreMovie = genreMovie.substring(0, genreMovie.length() - 2);
            }

            genre.setText(genreMovie);

            //Get similar series to this one, then display them into a recyclerview
            ArrayList<SeriesItem> similarSeriesItem;
            similarSeriesItem = SeriesItemDataExtract.getDataSeries(jsonObj.getJSONObject("recommendations"));

            if (similarSeriesItem.size() > 0) {
                similarSeriesRecyclerview.setAdapter(new SeriesRecyclerAdapter(DetailSerieActivity.this, similarSeriesItem));
                similarSeriesRecyclerview.setHasFixedSize(true);
                similarSeriesRecyclerview.setLayoutManager(new LinearLayoutManager(DetailSerieActivity.this, LinearLayoutManager.HORIZONTAL, false));
            } else {
                similarSerieLayout.setVisibility(View.GONE);
            }

            ArrayList<CastItem> castItems;
            castItems = CastDataExtract.getDataCast(jsonObj.getJSONObject("credits"));

            //Get the casts of the serie, then display them into a recyclerview
            if (castItems.size() > 0) {
                castRecyclerview.setAdapter(new CastRecyclerAdapter(DetailSerieActivity.this, castItems));
                castRecyclerview.setHasFixedSize(true);
                castRecyclerview.setLayoutManager(new LinearLayoutManager(DetailSerieActivity.this, LinearLayoutManager.HORIZONTAL, false));
            } else {
                castLayout.setVisibility(View.GONE);
            }

            //Get Videos of the serie
            ArrayList<VideosItem> videosItems;
            videosItems = VideoDataExtract.getDataVideo(jsonObj.getJSONObject("videos"));

            //If the API return no video, so the button of trailer is GONE
            //else we search for the first trailer,
            //if there isn't we search for Teaser,
            //if there isn't we search for Featurette,
            //if there isn't we search for Behind the Scenes,
            //Than we display the first video found

            if (videosItems.size() == 0) {
                btnWatchTrailer.setVisibility(View.GONE);
            } else {
                VideosItem video = null;
                int indexTeaser = -1, indexFeaturette = -1, indexBTS = -1;
                for (int i = 0; i < videosItems.size(); i++) {
                    if (videosItems.get(i).getTypeVideo().equals("Trailer")) {
                        video = videosItems.get(i);
                        break;
                    } else if (videosItems.get(i).getTypeVideo().equals("Teaser") && indexTeaser == -1) {
                        indexTeaser = i;
                    } else if (videosItems.get(i).getTypeVideo().equals("Featurette") && indexFeaturette == -1) {
                        indexFeaturette = i;
                    } else if (videosItems.get(i).getTypeVideo().equals("Behind the Scenes") && indexBTS == -1) {
                        indexBTS = i;
                    }
                }

                if (video == null) {
                    if (indexTeaser != -1) {
                        video = videosItems.get(indexTeaser);
                    } else if (indexFeaturette != -1) {
                        video = videosItems.get(indexFeaturette);
                    } else if (indexBTS != -1) {
                        video = videosItems.get(indexBTS);
                    } else {
                        video = videosItems.get(0);
                    }
                }

                if (video != null) {
                    VideosItem finalVideo = video;
                    btnWatchTrailer.setOnClickListener(view -> {
                        if (finalVideo.getSite().equals("YouTube")) {
                            StaticMethods.watchYoutubeVideo(DetailSerieActivity.this, finalVideo.getKeyVideo());
                        } else if (finalVideo.getSite().equals("Vimeo")) {
                            StaticMethods.watchVimeoVideo(DetailSerieActivity.this, finalVideo.getKeyVideo());
                        }
                    });
                }

            }


        } catch (Exception e) {

        }
    }

}