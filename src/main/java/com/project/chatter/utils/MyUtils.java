package com.project.chatter.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.project.chatter.BaseApplication;
import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.bean.ProductBean;
import com.project.chatter.pay.InsufficientChargeDialogFragment;
import com.project.chatter.pay.PayCallBack;
import com.project.chatter.pay.PayManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.project.chatter.constant.Constants.DEBUG_MODE_LOG;


public class MyUtils {

    private static final String TAG = "MyUtils";


    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param defaultId
     * @param path
     * @param iv
     */
    public static void loadImage(int defaultId, String path, ImageView iv) {
        Glide.with(BaseApplication.getAppContext())
                .load(path)
                .apply(new RequestOptions()
                        .placeholder(defaultId)
                        .error(defaultId)
                        .centerCrop()//中心切圖, 會填滿
                        .fitCenter()//中心fit, 以原本圖片的長寬為主
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                )
                .into(iv);

    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void log(int type, String tag, String msg) {
        if (DEBUG_MODE_LOG) {
            switch (type) {
                case 1:
                    Log.e(tag, msg);
                    break;
                default:
                    Log.i(tag, msg);
                    break;
            }
        }
    }

    public static boolean isSelfAnchor(){
            return false;
    }



    public static int getMaxHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.heightPixels;
    }






    public interface DiamondDialogCallBack{
        void back(String way);
    }

    public static void ShowChargeDiamondDialog(Context context, DiamondDialogCallBack callBack) {
        ShowChargeDiamondDialog(context,"",callBack);
    }
    public static void ShowChargeDiamondDialog(Context context, String title, final DiamondDialogCallBack callBack) {
            InsufficientChargeDialogFragment dialogFragment = new InsufficientChargeDialogFragment((Activity) context,new InsufficientChargeDialogFragment.CallBack() {
                @Override
                public void callBack() {
                    if (null != callBack){
                        callBack.back("");
                    }
                }

                @Override
                public void close() {

                }
            });
            dialogFragment.show(((BaseActivityChatter)context).getSupportFragmentManager(), "MyDialogFragment");
    }

    public static void ShowPayDialog(Context context, ProductBean mProductBean) {
        ShowPayDialog(context,mProductBean,false,null);
    }

    public static void ShowPayDialog(final Context context, final ProductBean mProductBean, boolean isSub, final ActionCallBack actionCallBack) {
     if (null != context && context instanceof BaseActivityChatter){
             PayManager.getInstance().init((BaseActivityChatter)context);
             PayManager.getInstance().Pay(mProductBean, new PayCallBack.out() {
                 @Override
                 public void paySuccess() {
                     ChatterToast.Toast("Successful");

                     if (null != actionCallBack){
                         actionCallBack.back();
                     }
                 }
             });
     }
    }


    public interface ActionCallBack{
        void back();
    }


    public static String generateSign(String mid,long timestamp,String uid,String secretKey){
        String sign = "";
        sign = md5(mid+timestamp+uid+secretKey);
        return sign;
    }
}


