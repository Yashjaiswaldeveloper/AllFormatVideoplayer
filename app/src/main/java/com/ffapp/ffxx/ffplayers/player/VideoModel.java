package com.ffapp.ffxx.ffplayers.player;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoModel implements Serializable,Comparable {

    String videoTitle;
    String videoDuration;
    Uri videoUri;
    ArrayList<String> alvideopath;
    public ArrayList<String> getAlvideopath() {
        return alvideopath;
    }

    public void setAlvideopath(ArrayList<String> alvideopath) {
        this.alvideopath = alvideopath;
    }


    public int getGetViewType() {
        return getViewType;
    }

    public void setGetViewType(int getViewType) {
        this.getViewType = getViewType;
    }

    int getViewType;

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }
    public int getAllImagePathSize() {
        return alvideopath.size();
    }
    @Override
    public int compareTo(Object o) {
        int comparable=((VideoModel) o).getAllImagePathSize();
        return comparable;
    }
}