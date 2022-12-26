package com.halim.flixflex.ClassesUtils;

/**
 *
 * Class that contains all our URLs for our API
 *
 */

public class URLs {
    //Image Base URL
    public static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500";

    //Vimeo watch
    public static String VIMEO_WATCH = "https://vimeo.com/";

    //API Key
    public static String API_KEY = "848f1c3fc0eaf715c7a433d9b38c4af6";

    //Recommanded movie to watch per 20
    public static String RECOMMENDED_MOVIES_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US&page=";

    //Recommanded Series to watch per 20
    public static String RECOMMENDED_SERIES_URL = "https://api.themoviedb.org/3/tv/popular?api_key=" + API_KEY + "&language=en-US&page=";

    //Top rated Movies per 5
    public static String TOP_RATED_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY + "&page=1";

    //Top rated Series per 5
    public static String TOP_RATED_SERIES_URL = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + API_KEY + "&page=1";


    //Detail movie with video and recommandations and cast
    public static String DETAIL_MOVIE_URL(int idMovie) {
        return "https://api.themoviedb.org/3/movie/" + idMovie + "?api_key=" + API_KEY + "&language=en-US&append_to_response=recommendations,videos,credits";
    }

    //Detail serie with video and recommandations and cast
    public static String DETAIL_SERIE_URL(int idSerie) {
        return "https://api.themoviedb.org/3/tv/" + idSerie + "?api_key=" + API_KEY + "&language=en-US&append_to_response=recommendations,videos,credits";
    }

    //Search movies
    public static String SEARCH_MOVIES_URL(String textSearch) {
        return "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + textSearch + "&sort_by=popularity.desc&page=";
    }

    //Search series
    public static String SEARCH_SERIES_URL(String textSearch) {
        return "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&query=" + textSearch + "&sort_by=popularity.desc&page=";
    }

}
