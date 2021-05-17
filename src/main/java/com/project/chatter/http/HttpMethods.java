package com.project.chatter.http;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.project.chatter.BaseApplication;
import com.project.chatter.constant.Constants;
import com.project.chatter.data.DataManager;
import com.project.chatter.fragment.HomeFragmentChatter;
import com.project.chatter.utils.CountryCodeUilts;
import com.project.chatter.utils.MyUtils;
import com.project.chatter.utils.NetUtil;
import com.project.chatter.utils.Preferences;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpMethods {

    private static final String TAG = "HttpMethods";
    private Retrofit mRetrofit;
    private static final int DEFAULT_TIMEOUT = 10;
    private static HttpMethods INSTANCE;

    String appCode =  Preferences.getInstacne().getMID();

    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                MyUtils.log(0, TAG, message);
            }
        });
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request;
                        String mid = Preferences.getInstacne().getMID();
                        String uid = Preferences.getInstacne().getUid();
                        long timestamp = System.currentTimeMillis();
                        String secretKey = Preferences.getInstacne().getSecretKey();
                        String sign = MyUtils.generateSign(mid,timestamp,uid,secretKey);


                    MyUtils.log(0,"Header",
                            "\nmid="+mid+
                                    "\nuid="+uid+
                                    "  \ntimestamp = "+timestamp +
                                    " \nsecretKey = "+secretKey+
                                    " \nsign = "+sign);
                    request = chain.request()
                            .newBuilder()
                            .addHeader("mid",mid)
                            .addHeader("uid",uid)
                            .addHeader("timestamp",timestamp+"")
                            .addHeader("sign",sign)
                            .build();
                return chain.proceed(request);
            }
        };
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();
    }

    public static HttpMethods getInstance() {
        if (INSTANCE == null) {
            synchronized (TAG) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpMethods();
                }
            }
        }
        return INSTANCE;
    }

    public String getBaseUrl() {
        return Constants.BASE_URL;
    }

    private <T> void toSubscribe(Observable<T> o, HttpObserver<T> s) {
        o.retry(2, new Predicate<Throwable>() {
            @Override
            public boolean test(@NonNull Throwable throwable) throws Exception {
                return NetUtil.isConnected(BaseApplication.getAppContext()) &&
                        (throwable instanceof SocketTimeoutException ||
                                throwable instanceof ConnectException ||
                                throwable instanceof ConnectTimeoutException ||
                                throwable instanceof TimeoutException);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void userList(int pageNumber, int pageSize, int mode,int refresh,HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNumber);
        map.put("pageSize", pageSize);
        HashMap<String, Object> query = new HashMap<>();
        query.put("refresh", refresh);
        if (mode == HomeFragmentChatter.MODE_NEW){
            query.put("listType", "newHost");
        }
        map.put("query",query);
        Observable observable = mRetrofit.create(HttpService.class).userList(map);
        toSubscribe(observable, observer);
    }

    public void chatStatus(HttpObserver observer) {
        Observable observable = mRetrofit.create(HttpService.class).chatStatus();
        toSubscribe(observable, observer);
    }

    public void userInfo(String uid, HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        Observable observable = mRetrofit.create(HttpService.class).userInfo(map);
        toSubscribe(observable, observer);
    }


    public void gPay(String code, String value, String productId, int type, HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("value", value);
        map.put("productId", productId);
        map.put("type",  type);
        Observable observable = mRetrofit.create(HttpService.class).gPay(map);
        toSubscribe(observable, observer);
    }



    public void saveCallAndRecevieRecord(String code, String toUid,
                                         HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
//        map.put("relateId", 0);
//        map.put("remark", "");
        map.put("toUid", toUid);
        Observable observable = mRetrofit.create(HttpService.class).saveRecord(map);
        toSubscribe(observable, observer);
    }

    public void payChat(long uid, String diamond, HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","PC");
        map.put("remoteUid",uid);
        map.put("type","");
        map.put("value",diamond);

        Observable observable = mRetrofit.create(HttpService.class).payChat(map);
        toSubscribe(observable, observer);
    }

    public void reportPurchaseError(String code, int diamond, String money, String productId,
                                    int type, String value, HttpObserver observer) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("code",code);
        map.put("diamond",diamond);
        map.put("money",money);
        map.put("productId",productId);
        map.put("type",type);
        map.put("value",value);
        Observable observable = mRetrofit.create(HttpService.class).reportPurchaseError(map);
        toSubscribe(observable,observer);

    }

    public void commitSocket(String key, String requestId, HttpObserver observer) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("requestId",requestId);
        map.put("key",key);
        Observable observable = mRetrofit.create(HttpService.class).commitSocket(map);
        toSubscribe(observable,observer);
    }

    public void saveBuyClickRecord( String productId,
                                    HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "clickProduct");
        map.put("remark", productId);
        Observable observable = mRetrofit.create(HttpService.class).saveRecord(map);
        toSubscribe(observable, observer);
    }



    public void callUser(String uid, HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("remoteUid", uid);
        String agoId = DataManager.getInstance().getAgoId();
        if (!TextUtils.isEmpty(agoId)){
            map.put("agoId", agoId);
        }
        Observable observable = mRetrofit.create(HttpService.class).callUser(map);
        toSubscribe(observable, observer);
    }

    public void answerUser(String uid, String roomId,int status,HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("remoteUid", uid);
        map.put("roomId", roomId);
        map.put("status", status);
        Observable observable = mRetrofit.create(HttpService.class).answerUser(map);
        toSubscribe(observable, observer);
    }

    public void closeChat(String uid, String roomId,HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("remoteUid", uid);
        map.put("roomId", roomId);
        Observable observable = mRetrofit.create(HttpService.class).closeChat(map);
        toSubscribe(observable, observer);
    }

    public void getAgoId(HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", appCode);
        Observable observable = mRetrofit.create(HttpService.class).getAgoId(map);
        toSubscribe(observable, observer);
    }


    public void getDiscountInfo(String type,HttpObserver observer) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCode",appCode);
        map.put("type",type);
        map.put("regionCode", CountryCodeUilts.getCountryCode());
        Observable observable = mRetrofit.create(HttpService.class).getDiscountInfo(map);
        toSubscribe(observable, observer);
    }

}
