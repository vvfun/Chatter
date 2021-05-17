package com.project.chatter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;


import com.project.chatter.activity.EmptyActivityChatter;
import com.project.chatter.bean.UserBean;
import com.project.chatter.utils.MyUtils;
import com.project.chatter.utils.Preferences;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_transparent);//全局设置主题颜色

                ClassicsHeader header = new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
                header.setAccentColorId(R.color.white);
                return header;

            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "Pull Down Refresh";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "Refreshing...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "Loading...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "Release";
        ClassicsHeader.REFRESH_HEADER_FINISH = "Refresh Finished";
        ClassicsHeader.REFRESH_HEADER_FAILED = "Refresh Failed";
        ClassicsHeader.REFRESH_HEADER_LASTTIME = "'Last Updated' MM-dd HH:mm";

        ClassicsFooter.REFRESH_FOOTER_PULLUP = "Pull Up Load More";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "Release";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "Loading...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "Refresh...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "Load More Finish";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "Load More Failed";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "All Load Finish";
    }


    private static Application appContext = null;
    public static boolean canCall = true;
    public static boolean canQuickChat = true;
    public static boolean canShowPushInfo = true;
    private Activity curActivity;

    public BaseApplication() {
        super();
    }

    public static void setAppContext(Application context) {
        appContext = context;
    }

    public static Application getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
