package com.halim.flixflex.Movies;

import java.io.Serializable;
import java.util.ArrayList;

//POJO class of Movies

public class MoviesItem implements Serializable {

    private int idMovie;
    private String titleMovie;
    private String descriptionMovie;
    private String backdropPicture;
    private String posterPicture;
    private Double voteAverage;
    private int voteCount;

    private Double budget;
    private Double popularity;
    private String releaseDate;
    private String runtime;
    private Double revenue;
    private ArrayList<MoviesItem> recommandationMovie;
    private ArrayList<Integer> genre;

    public MoviesItem() {
    }

    public MoviesItem(int idMovie,
                      String titleMovie,
                      String descriptionMovie,
                      String backdropPicture,
                      String posterPicture,
                      Double voteAverage,
                      int voteCount) {
        this.idMovie = idMovie;
        this.titleMovie = titleMovie;
        this.descriptionMovie = descriptionMovie;
        this.backdropPicture = backdropPicture;
        this.posterPicture = posterPicture;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getDescriptionMovie() {
        return descriptionMovie;
    }

    public void setDescriptionMovie(String descriptionMovie) {
        this.descriptionMovie = descriptionMovie;
    }

    public String getBackdropPicture() {
        return backdropPicture;
    }

    public void setBackdropPicture(String backdropPicture) {
        this.backdropPicture = backdropPicture;
    }

    public String getPosterPicture() {
        return posterPicture;
    }

    public void setPosterPicture(String posterPicture) {
        this.posterPicture = posterPicture;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public ArrayList<MoviesItem> getRecommandationMovie() {
        return recommandationMovie;
    }

    public void setRecommandationMovie(ArrayList<MoviesItem> recommandationMovie) {
        this.recommandationMovie = recommandationMovie;
    }

    public ArrayList<Integer> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
