package com.project.chatter.utils;

import android.content.SharedPreferences;

import com.project.chatter.BaseApplication;


public class Preferences {
    private static final String TAG = "Preferences";
    private static Preferences mConfig = null;
    private SharedPreferences mSettings;
    public static String KEY_CHATTER_UID = "chatter_uid";
    public static String KEY_CHATTER_MID = "chatter_mid";
    public static String KEY_CHATTER_SECRETKEY = "chatter_secretKey";

    public static Preferences getInstacne() {
        if (mConfig == null) {
            synchronized (TAG) {
                if (mConfig == null) {
                    mConfig = new Preferences();
                }
            }
        }
        return mConfig;
    }

    private Preferences() {
        mSettings = BaseApplication.getAppContext().getSharedPreferences("Config", 0);
    }

    public SharedPreferences getSettings() {
        if (mSettings == null) {
            mSettings = BaseApplication.getAppContext().getSharedPreferences("Config", 0);
        }
        return mSettings;
    }


    public void setValues(String key, String value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setValues(String key, boolean value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setValues(String key, long value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setValues(String key, int value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getValues(String key, String value) {
        return getSettings().getString(key, value);
    }

    public void setDevelopRegion(String region) {
        setValues("developregion", region);
    }

    public String getDevelopRegion() {
        return getValues("developregion", "");
    }


    public String getUid() {
        return getValues(KEY_CHATTER_UID, "");
    }

    public void setUid(String uid) {
        setValues(KEY_CHATTER_UID, uid);
    }

    public String getMID() {
        return getValues(KEY_CHATTER_MID, "");
    }

    public void setMID(String uid) {
        setValues(KEY_CHATTER_MID, uid);
    }

    public String getSecretKey() {
        return getValues(KEY_CHATTER_SECRETKEY, "");
    }

    public void setSecretKey(String secretkey) {
        setValues(KEY_CHATTER_SECRETKEY, secretkey);
    }

}
