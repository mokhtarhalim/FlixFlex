package com.halim.flixflex.Series;

import java.io.Serializable;

//POJO class of Series
public class SeriesItem implements Serializable {

    private int idSerie;
    private String titleSerie;
    private String descriptionSerie;
    private String backdropPicture;
    private String posterPicture;
    private Double voteAverage;
    private int voteCount;

    public SeriesItem(int idSerie,
                      String titleSerie,
                      String descriptionSerie,
                      String backdropPicture,
                      String posterPicture,
                      Double voteAverage,
                      int voteCount) {
        this.idSerie = idSerie;
        this.titleSerie = titleSerie;
        this.descriptionSerie = descriptionSerie;
        this.backdropPicture = backdropPicture;
        this.posterPicture = posterPicture;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }

    public String getTitleSerie() {
        return titleSerie;
    }

    public void setTitleSerie(String titleSerie) {
        this.titleSerie = titleSerie;
    }

    public String getDescriptionSerie() {
        return descriptionSerie;
    }

    public void setDescriptionSerie(String descriptionSerie) {
        this.descriptionSerie = descriptionSerie;
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
}
