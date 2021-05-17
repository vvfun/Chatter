package com.project.chatter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.project.chatter.activity.EmptyActivityChatter;
import com.project.chatter.utils.Preferences;

public class Chatter {

    public static void init(Application baseApp,String mid,String secretKey) {
        BaseApplication.setAppContext(baseApp);
        Preferences.getInstacne().setMID(mid);
        Preferences.getInstacne().setSecretKey(secretKey);
    }
    public static void setUserId(String uid){
        Preferences.getInstacne().setUid(uid);
    }
    public static boolean gotoPager(Context context,Class<?> pagerClass/*, Bundle bundle*/) {

        if (Activity.class.isAssignableFrom(pagerClass)) {
            Intent intent = new Intent(context, pagerClass);
//            if (bundle != null) {
//                intent.putExtras(bundle);
//            }
            context.startActivity(intent);
            return true;
        } else {
            String name = pagerClass.getName();
            Intent intent = new Intent(context, EmptyActivityChatter.class);
//            if (bundle != null) {
//                intent.putExtras(bundle);
//            }
            intent.putExtra("FRAGMENT_NAME", name);
            ((Activity)context).startActivity(intent);
            return true;
        }

    }

}


