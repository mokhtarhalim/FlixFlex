package com.halim.flixflex.Movies;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.halim.flixflex.ClassesUtils.StaticMethods;
import com.halim.flixflex.ClassesUtils.URLs;
import com.halim.flixflex.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {

    private Context context;

    private RecyclerView topRatedRecyclerview;
    private RecyclerView recommandedRecyclerview;
    private ProgressBar idPBLoading;
    private View no_content_layout_recommandation;
    private View no_content_layout_top_rated;
    private ShimmerFrameLayout recommandedshimmerViewContainer;
    private ShimmerFrameLayout topRatedShimmerViewContainer;

    private MoviesRecyclerAdapter moviesRecyclerAdapter;

    private ArrayList<MoviesItem> moviesRecommandedListShown;
    private ArrayList<MoviesItem> moviesTopRatedList;

    //Number of page of recommended movies if will increment each time the user reach the end of the recycler view
    private int page = 1;

    //It will check if the data is all print it from api
    private boolean dataFinished = false;

    public MoviesFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        context = rootView.getContext();

        findViewsById(rootView);

        moviesRecommandedListShown = new ArrayList<>();
        moviesTopRatedList = new ArrayList<>();

        recommandedshimmerViewContainer.setVisibility(View.VISIBLE);
        topRatedShimmerViewContainer.setVisibility(View.VISIBLE);

        //Get top rated movies from API
        getMoviesTopRated();

        //Get recommended movies from API
        getMoviesRecommanded(page);

        //Delect if user reach the end of the recyclerview, if yes we call API with incremented page
        recommandedRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollHorizontally(1)) {
                    page++;
                    getMoviesRecommanded(page);
                }
            }
        });

        return rootView;
    }

    private void findViewsById(View rootView) {
        topRatedRecyclerview = rootView.findViewById(R.id.topRatedRecyclerview);
        recommandedRecyclerview = rootView.findViewById(R.id.recommandedRecyclerview);
        idPBLoading = rootView.findViewById(R.id.idPBLoading);
        no_content_layout_recommandation = rootView.findViewById(R.id.no_content_layout_recommandation);
        recommandedshimmerViewContainer = rootView.findViewById(R.id.recommandedshimmerViewContainer);
        topRatedShimmerViewContainer = rootView.findViewById(R.id.topRatedShimmerViewContainer);
        no_content_layout_top_rated = rootView.findViewById(R.id.no_content_layout_top_rated);
    }

    private void getMoviesRecommanded(int page) {
        //Check if user has internet connection before calling API
        if (StaticMethods.isInternetAvailable()) {

            if (dataFinished) {
                // checking if the last api call get 0 data
                // displaying toast message in this case.
                Toast.makeText(context, "That's all the data..", Toast.LENGTH_SHORT).show();

                // hiding our shimmer.
                recommandedshimmerViewContainer.setVisibility(View.GONE);
                return;
            } else {
                idPBLoading.setVisibility(View.VISIBLE);
            }

            //Make our API Call using Volley
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.GET, URLs.RECOMMENDED_MOVIES_URL + page, response -> {

                ArrayList<MoviesItem> moviesRecommandedItems;
                try {
                    //Fetching our data using JSON
                    JSONObject jsonObject = new JSONObject(response);
                    moviesRecommandedItems = MoviesItemDataExtract.getDataMovies(jsonObject);
                    moviesRecommandedListShown.addAll(moviesRecommandedItems);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                idPBLoading.setVisibility(View.GONE);
                recommandedshimmerViewContainer.setVisibility(View.GONE);

                if (moviesRecommandedItems.size() > 0) {
                    if (page > 1) {
                        moviesRecyclerAdapter.notifyItemRangeInserted(moviesRecommandedListShown.size() - 1, moviesRecommandedItems.size());
                        recommandedRecyclerview.scrollToPosition(moviesRecommandedListShown.size() - moviesRecommandedItems.size());

                    } else {
                        //Display our result in Recyclerview using Adapter
                        moviesRecyclerAdapter = new MoviesRecyclerAdapter(context, moviesRecommandedListShown);
                        recommandedRecyclerview.setAdapter(moviesRecyclerAdapter);
                        recommandedRecyclerview.setHasFixedSize(true);
                        recommandedRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    }
                } else {
                    dataFinished = true;
                    recommandedRecyclerview.setVisibility(View.GONE);
                    no_content_layout_recommandation.setVisibility(View.VISIBLE);
                }

            }, error -> {
                Log.d("error", "error: " + error);
                recommandedshimmerViewContainer.setVisibility(View.GONE);
                idPBLoading.setVisibility(View.GONE);
                if (moviesRecommandedListShown.size() == 0)
                    recommandedRecyclerview.setVisibility(View.GONE);

                recommandedRecyclerview.setVisibility(View.GONE);
                no_content_layout_recommandation.setVisibility(View.VISIBLE);
            });
            requestQueue.add(postRequest);
        } else {
            recommandedshimmerViewContainer.setVisibility(View.GONE);
            recommandedRecyclerview.setVisibility(View.GONE);
            no_content_layout_recommandation.setVisibility(View.VISIBLE);
            StaticMethods.snackBarNoInternet(context);
        }
    }

    private void getMoviesTopRated() {
        //Check if user has internet connection before calling API
        if (StaticMethods.isInternetAvailable()) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.GET, URLs.TOP_RATED_MOVIES_URL, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    moviesTopRatedList = MoviesItemDataExtract.getDataMovies(jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                topRatedShimmerViewContainer.setVisibility(View.GONE);

                //Show only five first Top rated movies
                if (moviesTopRatedList.size() > 5)
                    moviesTopRatedList.subList(5, moviesTopRatedList.size()).clear();

                if (moviesTopRatedList.size() > 0) {
                    //Display our result in Recyclerview using Adapter
                    moviesRecyclerAdapter = new MoviesRecyclerAdapter(context, moviesTopRatedList);
                    topRatedRecyclerview.setAdapter(moviesRecyclerAdapter);
                    topRatedRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                } else {
                    topRatedRecyclerview.setVisibility(View.GONE);
                    topRatedShimmerViewContainer.setVisibility(View.GONE);
                    no_content_layout_top_rated.setVisibility(View.VISIBLE);
                }
            }, error -> {
                Log.d("error", "error: " + error);
                topRatedShimmerViewContainer.setVisibility(View.GONE);
                if (moviesTopRatedList.size() == 0) topRatedRecyclerview.setVisibility(View.GONE);

                topRatedRecyclerview.setVisibility(View.GONE);
                no_content_layout_top_rated.setVisibility(View.VISIBLE);
            });
            requestQueue.add(postRequest);
        } else {
            topRatedRecyclerview.setVisibility(View.GONE);
            topRatedShimmerViewContainer.setVisibility(View.GONE);
            no_content_layout_top_rated.setVisibility(View.VISIBLE);
            StaticMethods.snackBarNoInternet(context);
        }
    }
}
