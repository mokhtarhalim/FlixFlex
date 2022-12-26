package com.halim.flixflex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.halim.flixflex.Movies.MoviesFragment;
import com.halim.flixflex.Profil.ProfilFragment;
import com.halim.flixflex.Search.SearchFragment;
import com.halim.flixflex.Series.SeriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation;
    private Fragment active;
    private Fragment selectedFragment;
    private MoviesFragment moviesFragment;
    private SeriesFragment seriesFragment;
    private SearchFragment searchFragment;
    private ProfilFragment profilFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        findViewsById();

        //Initialize our first fragment who is MoviesFragment
        moviesFragment = new MoviesFragment();
        active = moviesFragment;

        getSupportFragmentManager().beginTransaction().add(R.id.flFragment, active, "Movie").commit();
        getSupportFragmentManager().beginTransaction().show(active).commit();

        bottom_navigation.setOnItemSelectedListener(item -> {
            //Each time we click on BottomNavigation we check if fragment is already added then we just show it,
            //Else we added to our SupportFragment Manager and hide the previous one, then we show the new one

            String tagFragment = "";
            int itemId = item.getItemId();
            if (itemId == R.id.btn_movie) {
                if (moviesFragment == null) {
                    moviesFragment = new MoviesFragment();
                }
                selectedFragment = moviesFragment;
                tagFragment = "Movie";
            } else if (itemId == R.id.btn_serie) {
                if (seriesFragment == null) {
                    seriesFragment = new SeriesFragment();
                }
                selectedFragment = seriesFragment;
                tagFragment = "Serie";
            } else if (itemId == R.id.btn_search) {
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                }
                selectedFragment = searchFragment;
                tagFragment = "Search";
            } else if (itemId == R.id.btn_profil) {
                if (profilFragment == null) {
                    profilFragment = new ProfilFragment();
                }
                selectedFragment = profilFragment;
                tagFragment = "Profil";
            }

            if (getSupportFragmentManager().findFragmentByTag(tagFragment) == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.flFragment, selectedFragment, tagFragment).commit();
            }

            // It will help to replace the
            // one fragment to other.
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().hide(active).show(selectedFragment).commit();
                active = selectedFragment;
            }

            return true;
        });

    }

    private void findViewsById() {
        bottom_navigation = findViewById(R.id.bottom_navigation);
    }

}