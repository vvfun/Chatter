package com.project.chatter.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.project.chatter.BaseApplication;

import java.util.Locale;

public class CountryCodeUilts {

    private static String region;

    public static final String getCountryCode() {

//        return "ja";
        if (!TextUtils.isEmpty(region)) {
            return region;
        }
        if (getDeveloperRegion()) {
            return region;
        }
        //String countryCode = activity.getResources().getConfiguration().locale.getCountry();

        //通过TelephonyManager获取SIM卡信息
        TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return BaseApplication.getAppContext().getResources().getConfiguration().locale.getCountry();//国家码
        }
        //SIM卡的ISO国家代码
        String simCountryIso = telephonyManager.getSimCountryIso();
//
        if (simCountryIso == null || simCountryIso.isEmpty()) {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = BaseApplication.getAppContext().getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = BaseApplication.getAppContext().getResources().getConfiguration().locale;
            }
            simCountryIso = locale.getCountry();//国家码
        }
        region = simCountryIso;
        return simCountryIso;
        // 测试 simCountryIso = "US"

    }

    public static final String getCountryOnlySim() {

        //String countryCode = activity.getResources().getConfiguration().locale.getCountry();

        //通过TelephonyManager获取SIM卡信息
        TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            //SIM卡的ISO国家代码
            String simCountryIso = telephonyManager.getSimCountryIso();

            return simCountryIso;
            // 测试 simCountryIso = "US"
        }
        return null;
    }

    public static final String getLanguageCode() {
        Locale locale =  BaseApplication.getAppContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        return language;
    }

    public static void setDevelopRegion(String region) {
        CountryCodeUilts.region = region;
    }

    private static boolean getDeveloperRegion() {
        if (isDebuggable()) {
            String developerRegion = Preferences.getInstacne().getDevelopRegion();
            if (!TextUtils.isEmpty(developerRegion)) {
                region = developerRegion;
                return true;
            }
        }
        return false;
    }

    public static boolean isDebuggable() {
        try {
            return (0 != (BaseApplication.getAppContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
