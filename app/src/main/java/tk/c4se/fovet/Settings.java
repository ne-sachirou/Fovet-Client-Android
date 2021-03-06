package tk.c4se.fovet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nesachirou on 15/03/06.
 */
public class Settings {
    private static Settings ourInstance;

    synchronized public static void init(Context context) {
        if (null == ourInstance) {
            ourInstance = new Settings(context);
        }
    }

    public static Settings getInstance() {
        return ourInstance;
    }

    private SharedPreferences pref;

    private Settings(Context context) {
        pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public int getUserId() {
        return pref.getInt("user_id", 0);
    }

    public void setUserId(int userId) {
        pref.edit().putInt("user_id", userId).apply();
    }

    public String getPassword() {
        return pref.getString("password", null);
    }

    public void setPassword(String password) {
        pref.edit().putString("password", password).apply();
    }

    public String getToken() {
        return pref.getString("token", null);
    }

    public void setToken(String token) {
        pref.edit().putString("token", token).apply();
    }

    public float getLaititude() {
        return pref.getFloat("latitude", 0);
    }

    public void setLatitude(float latitude) {
        pref.edit().putFloat("latitude", latitude).apply();
    }

    public float getLongitude() {
        return pref.getFloat("longitude", 0);
    }

    public void setLongitude(float longitude) {
        pref.edit().putFloat("longitude", longitude).apply();
    }

    public String getRestEndpoint() {
        return "http://415f58b4.ngrok.com";
    }
}
