package com.project.chatter.http;


import com.project.chatter.bean.Response;
import com.project.chatter.bean.CallHttpBean;
import com.project.chatter.bean.PayDiscountInfoDto;
import com.project.chatter.bean.RecordsBean;
import com.project.chatter.bean.UserBean;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by gigabud on 17-5-3.
 */

public interface HttpService {
    /**
     * @return
     */
    @POST("api/user/list")
    Observable<Response<RecordsBean<UserBean>>> userList(@Body HashMap<String, Object> map);

    /**
     * @return
     */
    @POST("api/chat/status")
    Observable<Response<ArrayList<UserBean>>> chatStatus();

    /**
     * @return
     */
    @POST("api/user/info")
    Observable<Response<UserBean>> userInfo(@Body HashMap<String, Object> map);

    /**
     * @return
     */
    @POST("api/save/record")
    Observable<Response> saveRecord(@Body HashMap<String, Object> map);


    @POST("api/gPay")
    Observable<Response> gPay(@Body HashMap<String, Object> map);

    @POST("api/pay/chat")
    Observable<Response<Integer>> payChat(@Body HashMap<String, Object> map);


    @GET("action/version?platform=android")
    @Headers("Content-Type:application/json")
    Observable<Response> getVersion();


    @POST("api/fail/record")
    Observable<Response> reportPurchaseError(@Body HashMap<String, Object> map);


    @POST("api/websocket/commit")
    Observable<Response> commitSocket(@Body HashMap<String, Object> map);


    @POST("api/video/call")
    Observable<Response<CallHttpBean>> callUser(@Body HashMap<String, Object> map);

    @POST("api/video/answer")
    Observable<Response<CallHttpBean>> answerUser(@Body HashMap<String, Object> map);

    @POST("api/video/close")
    Observable<Response> closeChat(@Body HashMap<String, Object> map);

    @POST("api/ago/id")
    Observable<Response<String>> getAgoId(@Body HashMap<String, Object> map);

    @POST("api/product/first/discount")
    Observable<Response<PayDiscountInfoDto>> getDiscountInfo(@Body HashMap<String, Object> map);
}
