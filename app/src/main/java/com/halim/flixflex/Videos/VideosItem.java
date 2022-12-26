package com.halim.flixflex.Videos;

import java.io.Serializable;

//POJO class of Videos
public class VideosItem implements Serializable {

    private String idVideo;
    private String site;
    private String keyVideo;
    private String nameVideo;
    private String typeVideo;

    public VideosItem(String idVideo, String site, String keyVideo, String nameVideo, String typeVideo) {
        this.idVideo = idVideo;
        this.site = site;
        this.keyVideo = keyVideo;
        this.nameVideo = nameVideo;
        this.typeVideo = typeVideo;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKeyVideo() {
        return keyVideo;
    }

    public void setKeyVideo(String keyVideo) {
        this.keyVideo = keyVideo;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }

    public String getTypeVideo() {
        return typeVideo;
    }

    public void setTypeVideo(String typeVideo) {
        this.typeVideo = typeVideo;
    }
}
