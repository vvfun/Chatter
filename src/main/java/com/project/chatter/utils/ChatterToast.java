package com.project.chatter.utils;


import android.widget.Toast;

import com.project.chatter.BaseApplication;

/**
 * Created by Administrator on 2019/6/13.
 */

public class ChatterToast {

    public static void Toast(String msg) {
        if (null != BaseApplication.getAppContext()){
            Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

}
