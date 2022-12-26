package com.halim.flixflex.Search;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.halim.flixflex.ClassesUtils.StaticMethods;
import com.halim.flixflex.ClassesUtils.URLs;
import com.halim.flixflex.Movies.MoviesItem;
import com.halim.flixflex.Movies.MoviesItemDataExtract;
import com.halim.flixflex.Movies.MoviesRecyclerAdapter;
import com.halim.flixflex.R;
import com.halim.flixflex.Series.SeriesItem;
import com.halim.flixflex.Series.SeriesItemDataExtract;
import com.halim.flixflex.Series.SeriesRecyclerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private Context context;

    private TextInputLayout search_layout;
    private TextInputEditText search_input;
    private RecyclerView moviesRecyclerview;
    private RecyclerView seriesRecyclerview;
    private ProgressBar idPBLoadingMovies;
    private ProgressBar idPBLoadingSeries;
    private ShimmerFrameLayout moviesshimmerViewContainer;
    private ShimmerFrameLayout seriesshimmerViewContainer;
    private View no_content_layout_movies;
    private View no_content_layout_series;
    private ConstraintLayout moviesMotionLayout;
    private ConstraintLayout seriesMotionLayout;
    private ConstraintLayout searchIllustrator;

    private ArrayList<MoviesItem> moviesItems;
    private MoviesRecyclerAdapter moviesRecyclerAdapter;

    //Number of page of searched movies it will increment each time the user reach the end of the recycler view
    private int pageMovies = 1;
    //It will check if the data is all print it from api of searched movies
    private boolean dataFinishedMovies;

    private ArrayList<SeriesItem> seriesItems;
    private SeriesRecyclerAdapter seriesRecyclerAdapter;

    //Number of page of searched series it will increment each time the user reach the end of the recycler view
    private int pageSeries = 1;
    //It will check if the data is all print it from api of searched series
    private boolean dataFinishedSeries;

    private String textSearch = "";

    public SearchFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        context = rootView.getContext();

        findViewsById(rootView);

        moviesItems = new ArrayList<>();
        seriesItems = new ArrayList<>();

        moviesshimmerViewContainer.setVisibility(View.VISIBLE);
        seriesshimmerViewContainer.setVisibility(View.VISIBLE);

        //Click of submit button of the Keyboard to start the search
        search_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(search_input.getText().toString().trim())) {
                    if (searchIllustrator.getVisibility() == View.VISIBLE) {
                        searchIllustrator.setVisibility(View.GONE);
                        moviesMotionLayout.setVisibility(View.VISIBLE);
                        seriesMotionLayout.setVisibility(View.VISIBLE);
                    }
                    moviesItems.clear();
                    seriesItems.clear();
                    no_content_layout_movies.setVisibility(View.GONE);
                    no_content_layout_series.setVisibility(View.GONE);
                    moviesRecyclerview.setVisibility(View.VISIBLE);
                    seriesRecyclerview.setVisibility(View.VISIBLE);
                    dataFinishedSeries = false;
                    dataFinishedMovies = false;
                    textSearch = search_input.getText().toString();
                    getMovies(search_input.getText().toString(), pageMovies);
                    getSeries(search_input.getText().toString(), pageSeries);
                }
                return true;
            }
            return false;
        });

        //Delect if user reach the end of the recyclerview, if yes we call API with incremented page of searched movies
        moviesRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollHorizontally(1)) {
                    pageMovies++;
                    getMovies(textSearch, pageMovies);
                }
            }
        });

        //Delect if user reach the end of the recyclerview, if yes we call API with incremented page of searched series
        seriesRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollHorizontally(1)) {
                    pageSeries++;
                    getSeries(textSearch, pageSeries);
                }
            }
        });

        return rootView;
    }

    private void findViewsById(View rootView) {
        search_layout = rootView.findViewById(R.id.search_layout);
        search_input = rootView.findViewById(R.id.search_input);
        moviesRecyclerview = rootView.findViewById(R.id.moviesRecyclerview);
        idPBLoadingMovies = rootView.findViewById(R.id.idPBLoadingMovies);
        moviesshimmerViewContainer = rootView.findViewById(R.id.moviesshimmerViewContainer);
        seriesRecyclerview = rootView.findViewById(R.id.seriesRecyclerview);
        idPBLoadingSeries = rootView.findViewById(R.id.idPBLoadingSeries);
        seriesshimmerViewContainer = rootView.findViewById(R.id.seriesshimmerViewContainer);
        no_content_layout_series = rootView.findViewById(R.id.no_content_layout_series);
        no_content_layout_movies = rootView.findViewById(R.id.no_content_layout_movies);
        moviesMotionLayout = rootView.findViewById(R.id.moviesMotionLayout);
        seriesMotionLayout = rootView.findViewById(R.id.seriesMotionLayout);
        searchIllustrator = rootView.findViewById(R.id.searchIllustrator);
    }

    private void getMovies(String text, int page) {
        //Check if user has internet connection before calling API
        if (StaticMethods.isInternetAvailable()) {

            if (dataFinishedMovies) {
                // checking if the last api call get 0 data
                // displaying toast message in this case.
                Toast.makeText(context, "That's all the data..", Toast.LENGTH_SHORT).show();

                // hiding our shimmer.
                moviesshimmerViewContainer.setVisibility(View.GONE);
                return;
            } else {
                idPBLoadingMovies.setVisibility(View.VISIBLE);
            }

            //Make our API Call using Volley
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.GET, URLs.SEARCH_MOVIES_URL(text) + page, response -> {

                ArrayList<MoviesItem> movies;
                try {
                    //Fetching our data using JSON
                    JSONObject jsonObject = new JSONObject(response);
                    movies = MoviesItemDataExtract.getDataMovies(jsonObject);
                    moviesItems.addAll(movies);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                idPBLoadingMovies.setVisibility(View.GONE);
                moviesshimmerViewContainer.setVisibility(View.GONE);

                if (moviesItems.size() > 0) {
                    if (page > 1) {
                        moviesRecyclerAdapter.notifyItemRangeInserted(moviesItems.size() - 1, movies.size());
                        moviesRecyclerview.scrollToPosition(moviesItems.size() - movies.size());

                    } else {
                        //Display our result in Recyclerview using Adapter
                        moviesRecyclerAdapter = new MoviesRecyclerAdapter(context, moviesItems);
                        moviesRecyclerview.setAdapter(moviesRecyclerAdapter);
                        moviesRecyclerview.setHasFixedSize(true);
                        moviesRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    }
                } else {
                    dataFinishedMovies = true;
                    moviesRecyclerview.setVisibility(View.GONE);
                    no_content_layout_movies.setVisibility(View.VISIBLE);
                }

            }, error -> {
                Log.d("error", "error: " + error);
                moviesshimmerViewContainer.setVisibility(View.GONE);
                idPBLoadingMovies.setVisibility(View.GONE);
                if (moviesItems.size() == 0)
                    moviesRecyclerview.setVisibility(View.GONE);

                moviesRecyclerview.setVisibility(View.GONE);
                no_content_layout_movies.setVisibility(View.VISIBLE);
            });
            requestQueue.add(postRequest);
        } else {
            moviesshimmerViewContainer.setVisibility(View.GONE);
            moviesRecyclerview.setVisibility(View.GONE);
            no_content_layout_movies.setVisibility(View.VISIBLE);
            StaticMethods.snackBarNoInternet(context);
        }
    }

    private void getSeries(String text, int page) {
        //Check if user has internet connection before calling API
        if (StaticMethods.isInternetAvailable()) {

            if (dataFinishedSeries) {
                // checking if the last api call get 0 data
                // displaying toast message in this case.
                Toast.makeText(context, "That's all the data..", Toast.LENGTH_SHORT).show();

                // hiding our shimmer.
                seriesshimmerViewContainer.setVisibility(View.GONE);
                return;
            } else {
                idPBLoadingSeries.setVisibility(View.VISIBLE);
            }

            //Make our API Call using Volley
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.GET, URLs.SEARCH_SERIES_URL(text) + page, response -> {

                ArrayList<SeriesItem> series;
                try {
                    //Fetching our data using JSON
                    JSONObject jsonObject = new JSONObject(response);
                    series = SeriesItemDataExtract.getDataSeries(jsonObject);
                    seriesItems.addAll(series);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                idPBLoadingSeries.setVisibility(View.GONE);
                seriesshimmerViewContainer.setVisibility(View.GONE);

                if (seriesItems.size() > 0) {
                    if (page > 1) {
                        seriesRecyclerAdapter.notifyItemRangeInserted(seriesItems.size() - 1, series.size());
                        seriesRecyclerview.scrollToPosition(seriesItems.size() - series.size());

                    } else {
                        seriesRecyclerAdapter = new SeriesRecyclerAdapter(context, seriesItems);
                        seriesRecyclerview.setAdapter(seriesRecyclerAdapter);
                        seriesRecyclerview.setHasFixedSize(true);
                        seriesRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    }
                } else {
                    dataFinishedSeries = true;
                    seriesRecyclerview.setVisibility(View.GONE);
                    no_content_layout_series.setVisibility(View.VISIBLE);
                }

            }, error -> {
                Log.d("error", "error: " + error);
                seriesshimmerViewContainer.setVisibility(View.GONE);
                idPBLoadingSeries.setVisibility(View.GONE);
                if (seriesItems.size() == 0)
                    seriesRecyclerview.setVisibility(View.GONE);

                seriesRecyclerview.setVisibility(View.GONE);
                no_content_layout_series.setVisibility(View.VISIBLE);
            });
            requestQueue.add(postRequest);
        } else {
            seriesshimmerViewContainer.setVisibility(View.GONE);
            seriesRecyclerview.setVisibility(View.GONE);
            no_content_layout_series.setVisibility(View.VISIBLE);
            StaticMethods.snackBarNoInternet(context);
        }
    }

}
