package com.project.chatter.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.project.chatter.bean.UserBean;
import com.project.chatter.utils.Preferences;

/**
 * Created by gigabud on 17-5-9.
 */

public class DataManager implements IDataManager {
    private static final String TAG = "DataManager";
    private static DataManager mDataManager;
    private Object mObject;

    private UserBean mMyUserInfo;
    private String mToken;



    public static IDataManager getInstance() {
        if (mDataManager == null) {
            synchronized (TAG) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }

    private DataManager() {

    }

    @Override
    public synchronized void saveMyUserInfo(UserBean myUserInfo) {
        Preferences.getInstacne().setValues("my_user_info", myUserInfo == null ? "" : new Gson().toJson(myUserInfo));
        mMyUserInfo = myUserInfo;
    }

    @Override
    public UserBean getMyUserInfo() {
        if (mMyUserInfo != null) {
            return mMyUserInfo;
        }
        String dataStr = Preferences.getInstacne().getValues("my_user_info", "");
        if (!TextUtils.isEmpty(dataStr)) {
            mMyUserInfo = new Gson().fromJson(dataStr, UserBean.class);
        }
        return mMyUserInfo;
    }

    @Override
    public void clearMyUserInfo() {
        mToken = null;
        mMyUserInfo = null;
        Preferences.getInstacne().setValues("token", "");
        Preferences.getInstacne().setValues("my_user_info", "");
        Preferences.getInstacne().setValues("user_list", "");
        Preferences.getInstacne().setValues("discover_list", "");
    }
    @Override
    public void setObject(Object object) {
        mObject = object;
    }

    @Override
    public Object getObject() {
        return mObject;
    }

    @Override
    public void setAgoId(String agoId) {
        Preferences.getInstacne().setValues("agoid", agoId);
    }

    @Override
    public String getAgoId(){
        return Preferences.getInstacne().getValues("agoid", "");
    }

}
