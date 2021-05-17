package com.project.chatter.call;

import com.project.chatter.bean.CallHttpBean;
import com.project.chatter.bean.UserBean;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.utils.MyUtils;

public class CallUtils {
    public static String TAG = "CallUtils";
    public static void callUser(UserBean callUser, final CallBack callBack) {
        MyUtils.log(1, TAG, "callUser");

        HttpMethods.getInstance().callUser(callUser.getUid()+"",
                new HttpObserver(new SubscriberOnNextListener<CallHttpBean>() {
                    @Override
                    public void onNext(CallHttpBean bean, String msg) {
                        if (null != callBack){
                            callBack.callBack(bean);
                        }
                    }
                }, new OnHttpErrorListener() {
                    @Override
                    public void onConnectError(Throwable e) {

                    }

                    @Override
                    public void onServerError(int errorCode, String errorMsg) {

                    }
                }));
    }


    public static void answerUser(UserBean peerUser, String roomId, boolean isAnswer, final CallBack callBack) {
        MyUtils.log(1, TAG, "answerUser");

        int status = isAnswer? 1:0;
        HttpMethods.getInstance().answerUser(peerUser.getUid()+"",roomId,status,
                new HttpObserver(new SubscriberOnNextListener<CallHttpBean>() {
                    @Override
                    public void onNext(CallHttpBean bean, String msg) {
                        if (null != callBack){
                            callBack.callBack(bean);
                        }
                    }
                }, new OnHttpErrorListener() {
                    @Override
                    public void onConnectError(Throwable e) {

                    }

                    @Override
                    public void onServerError(int errorCode, String errorMsg) {

                    }
                }));
    }

    public static void endCall(UserBean peerUser,String roomId) {
        MyUtils.log(1, TAG, "endCall");
        HttpMethods.getInstance().closeChat(peerUser.getUid()+"",roomId,
                new HttpObserver(new SubscriberOnNextListener() {
                    @Override
                    public void onNext(Object o, String msg) {

                    }
                }, new OnHttpErrorListener() {
                    @Override
                    public void onConnectError(Throwable e) {

                    }

                    @Override
                    public void onServerError(int errorCode, String errorMsg) {

                    }
                }));
    }


    public interface CallBack{
        void callBack(CallHttpBean bean);
    }


}
