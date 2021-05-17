package com.project.chatter.pay;

import android.app.Activity;
import android.content.Intent;

import com.project.chatter.activity.BaseActivityChatter;
import com.project.chatter.bean.ProductBean;
import com.project.chatter.bean.UserBean;
import com.project.chatter.data.DataManager;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;

import java.util.Timer;
import java.util.TimerTask;


public class PayManager {

    private static final String TAG = "PayManager";
    public static final int MODE_GOOGLE = 0;

    private static PayManager manager;
    private PayInterface pay;
    Activity mActivity;
    PayCallBack.out mCallBack;

    int mMode;

    public static PayManager getInstance(){
        if (null == manager){
            synchronized (TAG){
                if (null == manager) {
                    manager = new PayManager();
                }
            }
        }
        return manager;
    }



    public void init(Activity activity){
            init(activity,PayManager.MODE_GOOGLE);
    }

    public void init(Activity activity, int mode){
        mActivity = activity;
        mMode = mode;
        switch (mode){
            case MODE_GOOGLE:
                pay = new GooglePayImp();
                break;
        }
        pay.init(activity);
    }

    public void init(Activity activity, int mode, String params){
        mActivity = activity;
        mMode = mode;
        switch (mode){
            case MODE_GOOGLE:
                pay = new GooglePayImp();
                break;
        }
        pay.init(activity);
    }

    ProductBean mDiscountProduct ;
    ProductBean mCurrentProduct ;


    public void Pay(final ProductBean productBean, PayCallBack.out callBack){
        mCurrentProduct = productBean;

        if (mActivity instanceof BaseActivityChatter && !mActivity.isFinishing()){
//            ((BaseActivityPoChat)mActivity).showLoadingDialog();
        }
        mCallBack = callBack;
        pay.setCallBack(new PayCallBack.inter() {
            @Override
            public void paySuccess() {

                if (mActivity instanceof BaseActivityChatter){
//                    ((BaseActivityPoChat)mActivity).hideLoadingDialog();
                }
                UserBean myInfo = DataManager.getInstance().getMyUserInfo();
                myInfo.setDiamond(myInfo.getDiamond() + productBean.getDiamond());
                DataManager.getInstance().saveMyUserInfo(myInfo);

                mCallBack.paySuccess();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        /**
                         * 延时执行的代码
                         */
                        getUserInfo();
                    }
                },2000); // 延时2秒


            }

            @Override
            public void cancel() {
                if (mActivity instanceof BaseActivityChatter){
//                    ((BaseActivityPoChat)mActivity).hideLoadingDialog();
                }
//                showLimitDialog();
            }
        });

        pay.Pay(productBean);
        payClick();
    }





    public void setSkuType(String skuType){
        if (null != pay) {
            if (pay instanceof GooglePayImp) {
                ((GooglePayImp) pay).setSkuType(skuType);
            }
        }
    }

    public void onReturnResult(int requestCode, int resultCode, Intent data){
        if (null != pay){
            pay.onReturnResult(requestCode,resultCode,data);
        }
    }




    private void getUserInfo(){
        UserBean userBean = DataManager.getInstance().getMyUserInfo();
        if (null != userBean){
            HttpMethods.getInstance().userInfo(userBean.getUid()+"", new HttpObserver(new SubscriberOnNextListener<UserBean>() {
                @Override
                public void onNext(UserBean user, String msg) {
                    if (null != user){
                        DataManager.getInstance().saveMyUserInfo(user);
                    }

                    mCallBack.paySuccess();
                    if (mActivity instanceof BaseActivityChatter){
//                        ((BaseActivityPoChat)mActivity).hideLoadingDialog();
                    }
                }
            },/*mActivity,*/ (OnHttpErrorListener) mActivity));
        }
    }



    private void payClick() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HttpMethods.getInstance().saveBuyClickRecord(mCurrentProduct.getProductIosId(),new HttpObserver(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o, String msg) {

                    }

                }, (OnHttpErrorListener) mActivity) );
            }
        });
    }
}
