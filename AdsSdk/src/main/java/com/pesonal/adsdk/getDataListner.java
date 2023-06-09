package com.pesonal.adsdk;

import org.json.JSONObject;

public interface getDataListner {

    void onsuccess();

    void onSuccess();

    void onUpdate(String url);

    void onRedirect(String url);

    void onReload();

    void onGetExtradata(JSONObject extraData);

    void reloadActivity();

    void ongetExtradata(JSONObject extraData);
}
