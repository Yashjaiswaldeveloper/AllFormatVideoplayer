package com.ffapp.ffxx.ffplayers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class Utilss {
    public static List<Country> list;
    public static List<Country_Code> list_Country;
    public static void loginClientInfo(ClientInfo clientInfo) {
        UnifiedSDK unifiedSDK = UnifiedSDK.getInstance(clientInfo);

        AuthMethod authMethod = AuthMethod.anonymous();
        unifiedSDK.getBackend().login(authMethod, new Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                Log.e("Vpin", "success: " + user.getAccessToken() + " " + user.getSubscriber());

            }

            @Override
            public void failure(@NonNull VpnException e) {
                Log.e("Vpin", "failure: ", e);
            }
        });
    }

    public static com.fftwo.ffftwo.gametwo.CountryModal getCountryFlagPath(String a, Activity activity) {
        String jObject = loadJSONFromAsset("country_flag.json", activity);
        try {
            JSONObject object = new JSONObject(jObject);
            JSONArray array = object.getJSONArray("country_flag");
            for (int i = 0; i < array.length(); i++) {
                JSONObject list = array.getJSONObject(i);
                String path = list.getString("url");
                String name = list.getString("Name");
                String code = list.getString("Code");
               com.fftwo.ffftwo.gametwo.CountryModal countryModal = new com.fftwo.ffftwo.gametwo.CountryModal();
                countryModal.setCode(code);
                countryModal.setName(name);
                countryModal.setPathFlag(path);
                if (a.equalsIgnoreCase(code)) {
                    return countryModal;
                }
            }

        } catch (JSONException e) {

        }
        return null;
    }

    public static String loadJSONFromAsset(String a, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(a);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static String humanReadableByteCountOld(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
