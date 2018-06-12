package com.minhduc.goiymonan.User;

/**
 * Created by minhduc on 12/9/2017.
 */

public class VideoYoutube {

    private String IdVideo;
    private String Title;
    private String UrlVideo;


    public VideoYoutube(String idVideo, String title, String urlVideo) {
        IdVideo = idVideo;
        Title = title;
        UrlVideo = urlVideo;
    }

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrlVideo() {
        return UrlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        UrlVideo = urlVideo;
    }
}