package com.ffapp.ffxx.ffplayers.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FolderModel implements Serializable,Comparable {
    public String name;
    public String path;
    public int viewType;
    ArrayList<String> alvideopath;
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getAlvideopath() {
        return alvideopath;
    }

    public void setAlvideopath(ArrayList<String> alvideopath) {
        this.alvideopath = alvideopath;
    }
    public int getAllImagePathSize() {
        return alvideopath.size();
    }


    @Override
    public int compareTo(Object o) {
        int comparable=((FolderModel) o).getAllImagePathSize();
        return comparable;
    }
}
