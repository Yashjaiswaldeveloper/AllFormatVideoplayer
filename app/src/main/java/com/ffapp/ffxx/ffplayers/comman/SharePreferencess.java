package com.ffapp.ffxx.ffplayers.comman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharePreferencess {
    private static final String TAG = "TAG";
    static SharedPreferences preferences;

    public SharePreferencess(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    public void setReapetVideo(boolean repeat) {
        preferences.edit().putBoolean("rep", repeat).apply();
    }

    public boolean getReapetVideo() {
        return preferences.getBoolean("rep", false);
    }

    public void setVideoVisible(boolean check) {
        preferences.edit().putBoolean("check", check).apply();
    }

    public boolean getVideoVisible() {
        return preferences.getBoolean("check", false);
    }

    public void setAudioTrack(boolean track) {
        preferences.edit().putBoolean("track", track).apply();
    }

    public boolean getAudioTrack() {
        return preferences.getBoolean("track", false);
    }

    public void setPassword(String password) {
        preferences.edit().putString("pass", password).apply();
    }

    public String getPassword() {
        return preferences.getString("pass", "");
    }

    public void setlogin(boolean pass) {
        preferences.edit().putBoolean("log", pass).apply();
    }

    public boolean getLogin() {
        return preferences.getBoolean("log", false);
    }

    public void setAutoPlay(boolean play) {
        preferences.edit().putBoolean("auto", play).apply();
    }

    public void setOrientation(String orientation) {
        preferences.edit().putString("ori", orientation).apply();

    }

    public String getOrientation() {
        return preferences.getString("ori", "1");
    }

    public boolean getAutoPlay() {
        return preferences.getBoolean("auto", true);
    }

    public void setRecoverVideoPath(String path) {

        ArrayList<String> paths = getRecoverVideoPath();
        if (paths != null) {
            paths.add(path);
        } else {
            paths = new ArrayList<>();
            paths.add(path);
        }

        Gson gson = new Gson();
        String json = gson.toJson(paths);
        preferences.edit().putString("recover_path", json).apply();
    }

    public ArrayList<String> getRecoverVideoPath() {
        Gson gson = new Gson();
        String json = preferences.getString("recover_path", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> strings = gson.fromJson(json, type);
        if (strings != null) {

        }
        return strings;
    }

    public void updateRecoverVideoPath(ArrayList<String> paths) {


        Log.e(TAG, "updateRecoverVideoPath: " + paths.size());

        for (int i = 0; i < paths.size(); i++) {
            Log.e(TAG, "updateRecoverVideoPath: paths: " + paths.get(i));
        }

        Gson gson = new Gson();
        String json = gson.toJson(paths);
        preferences.edit().putString("recover_path", json).apply();
    }

    public void setPrivateVideoPath(String path) {


        ArrayList<String> paths = getPrivateVideoPath();
        if (paths != null) {
            paths.add(path);
        } else {
            paths = new ArrayList<>();
            paths.add(path);
        }

        Gson gson = new Gson();
        String json = gson.toJson(paths);
        preferences.edit().putString("path", json).apply();
    }

    public ArrayList<String> getPrivateVideoPath() {

        Gson gson = new Gson();
        String json = preferences.getString("path", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> strings = gson.fromJson(json, type);
        if (strings != null) {
            Log.e(TAG, "getPrivateVideoPath: Size " + strings.size());
        }
        return strings;
    }

    public void updatePrivateVideoPath(ArrayList<String> paths) {

        Gson gson = new Gson();
        String json = gson.toJson(paths);
        preferences.edit().putString("path", json).apply();
    }

    public void setResumeDefault(String status) {

        preferences.edit().putString("default",status).apply();
    }
    public String getResumeDefault()
    {

        return preferences.getString("default","1");
    }


    public void setResume(String id, int time) {
        preferences.edit().putInt(id, time).apply();
    }

    public int getResume(String id) {
        return preferences.getInt(id, 0);
    }

    public void setTageVideo(String id,boolean tag)
    {
        preferences.edit().putBoolean(id,tag).apply();
    }
    public boolean getTagVideo(String id)
    {
      return preferences.getBoolean(id,false);
    }
    public void setChangeLayout(boolean layout)
    {
        preferences.edit().putBoolean("lay",layout).apply();
    }

    public void setNewTagCount(String id,int count)
    {
        preferences.edit().putInt(id,count).apply();
    }
    public int getNewTagCount(String id)
    {
        return preferences.getInt(id,0);
    }

    public void setNewTag(String id,int num)
    {
        preferences.edit().putInt(id,num).apply();
    }

    public int getNewTag(String id)
    {
        return preferences.getInt(id,0);
    }

    public boolean getChangeLayout()
    {
        return preferences.getBoolean("lay",false);
    }

}
