package com.project.chatter.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.project.chatter.constant.Constants;
import com.project.chatter.R;
import com.project.chatter.bean.ProductBean;
import com.project.chatter.bean.UserBean;
import com.project.chatter.data.DataManager;
import com.project.chatter.http.HttpMethods;
import com.project.chatter.http.HttpObserver;
import com.project.chatter.http.OnHttpErrorListener;
import com.project.chatter.http.SubscriberOnNextListener;
import com.project.chatter.utils.ChatterToast;
import com.project.chatter.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class GooglePayImp implements PayInterface{
    Context mContext;
    private final static String TAG = "GooglePayImp";
    private BillingClient mBillingClient;
    private ProductBean mProductBean;

    private PayCallBack.inter mPayCallBack;



    public String SkuType = BillingClient.SkuType.INAPP;


    public void setSkuType(String skuType) {
        SkuType = skuType;
    }














    private void reportPurchaseError(String code, int diamond, String money, String productId,
                                     String errror){
        HttpMethods.getInstance().
                reportPurchaseError(code,diamond,money,productId, Constants.DEBUG_MODE_FOR_MONEY?3:1,errror,
                        new HttpObserver(new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o, String msg) {

                            }
                        },mContext, (OnHttpErrorListener) mContext));


    }




    @Override
    public void init(Activity activity) {
        mContext = activity;
        mBillingClient = BillingClient.newBuilder(activity).enablePendingPurchases().
                setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                        GPayCallBack(billingResult, purchases);
                    }
                }).build();
    }

    @Override
    public void Pay(ProductBean productBean) {
        if (null != mBillingClient){
            mProductBean = productBean;
            mBillingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    MyUtils.log(0,TAG,"mProductBean=="+mProductBean.getProductIosId()+
                            "  getResponseCode=="+billingResult.getResponseCode());
                    if (null != billingResult && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                        queryGoogleHistoryAndConsume();

                        queryGoogleSkuAndPay();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    MyUtils.log(0,TAG,"onBillingServiceDisconnected");
                }
            });
        }
    }

    @Override
    public void setCallBack(PayCallBack.inter callBack) {
        mPayCallBack = callBack;
    }

    @Override
    public void onReturnResult(int requestCode, int resultCode, Intent data) {

    }






    public void GPayCallBack(BillingResult billingResult, @Nullable List<Purchase> purchases){
        if (null != purchases){
            Log.e(TAG,"onPurchasesUpdated   "+purchases.size());
        }
        String onPurchasesUpdated = "onPurchasesUpdated   getResponseCode=="+billingResult.getResponseCode()+
                "   getDebugMessage=="+billingResult.getDebugMessage();

        Log.e(TAG,onPurchasesUpdated);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            if (null != mPayCallBack){
                mPayCallBack.cancel();
            }
            if (purchases != null && purchases.size() > 0){
                for (Purchase purchase : purchases) {
                    reportPurchaseError("GPay",mProductBean.getDiamond(),mProductBean.getMoney()+"",
                            mProductBean.getProductIosId(),"GPay_USER_CANCELED:"+purchase.getOrderId()+"  ResponseCode = "+billingResult.getResponseCode()+
                                    "   DebugMessage = "+billingResult.getDebugMessage());
                }
            }else {
                reportPurchaseError("GPay",mProductBean.getDiamond(),mProductBean.getMoney()+"",
                        mProductBean.getProductIosId(),"GPay_USER_CANCELED:ResponseCode = "+billingResult.getResponseCode()+
                                "   DebugMessage = "+billingResult.getDebugMessage());
            }

        } else {
            // Handle any other error codes.

            if (purchases != null && purchases.size() > 0){
                for (Purchase purchase : purchases) {
                    reportPurchaseError("GPay",mProductBean.getDiamond(),mProductBean.getMoney()+"",
                            mProductBean.getProductIosId(),"GPay_OTHER_ERROR:"+purchase.getOrderId()+"  ResponseCode = "+billingResult.getResponseCode()+
                                    "   DebugMessage = "+billingResult.getDebugMessage());
                }
            }else {

                reportPurchaseError("GPay",mProductBean.getDiamond(),mProductBean.getMoney()+"",
                        mProductBean.getProductIosId(),"GPay_OTHER_ERROR:ResponseCode = "+billingResult.getResponseCode()+
                                "   DebugMessage = "+billingResult.getDebugMessage());
            }
        }
    }


    private void queryGoogleSkuAndPay() {
        if (null != mProductBean){
            List<String> skuList = new ArrayList<>();
            MyUtils.log(0,TAG,mProductBean.getProductIosId());
            skuList.add(mProductBean.getProductIosId());
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(SkuType);
            mBillingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (null != skuDetailsList && skuDetailsList.size() > 0){
                                SkuDetails skuDetails = skuDetailsList.get(0);
                                MyUtils.log(0,TAG,
                                        "\ngetTitle=="+skuDetails.getTitle()+
                                                "\ngetSku=="+skuDetails.getSku()+
                                                "\ngetType=="+skuDetails.getType()+
                                                "\ngetPrice=="+skuDetails.getPrice());



                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetails)
                                        .build();
                                BillingResult responseCode = mBillingClient.launchBillingFlow((Activity) mContext,flowParams);
                                MyUtils.log(0,TAG,"getResponseCode=="+responseCode.getResponseCode()+
                                        "\ngetDebugMessage=="+responseCode.getDebugMessage());


                            }
                        }
                    });
        }
    }

    private void queryGoogleHistoryAndConsume() {
        mBillingClient.queryPurchaseHistoryAsync(SkuType, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList) {
                if (null != purchaseHistoryRecordList && purchaseHistoryRecordList.size() > 0){

                    for (int i = 0; i < purchaseHistoryRecordList.size(); i++) {
                        PurchaseHistoryRecord purchase = purchaseHistoryRecordList.get(i);
                        String str = "\ngetPurchaseToken=="+purchase.getPurchaseToken()+
                                "\ngetDeveloperPayload=="+purchaseHistoryRecordList.get(i).getDeveloperPayload()+
                                "\ngetOriginalJson=="+purchaseHistoryRecordList.get(i).getOriginalJson()+
                                "\ngetSignature=="+purchaseHistoryRecordList.get(i).getSignature();
                        MyUtils.log(0,TAG,str);

                        String token = purchaseHistoryRecordList.get(i).getPurchaseToken();
                        String payload = purchaseHistoryRecordList.get(i).getDeveloperPayload();


                        consumeGoogleGood(token, payload);
                    }
                }
            }
        });
    }

    private void handlePurchase(final Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                consumeGoogleGood(purchase);

            }
        }else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.
            ChatterToast.Toast(mContext.getResources().getString(R.string.chatter_google_pending_hint));
            Log.e(TAG, "Purchase.PurchaseState.PENDING");

        }else {
            ChatterToast.Toast(mContext.getResources().getString(R.string.chatter_google_error));
        }
    }

    private void consumeGoogleGood(Purchase purchase) {
        String token = purchase.getPurchaseToken();
        String payload =  purchase.getDeveloperPayload();
        ConsumeParams consumeParams = ConsumeParams.newBuilder().
                setPurchaseToken(token).
                setDeveloperPayload(payload).build();

        consume(purchase, true, consumeParams);
    }

    private void consumeGoogleGood(String token , String payload) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder().
                setPurchaseToken(token).
                setDeveloperPayload(payload).build();

        consume(null, false, consumeParams);
    }

    private void consume(final Purchase purchase, final boolean isNeedAddDiamond, ConsumeParams consumeParams) {
        mBillingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                String onConsumeResponse ="onConsumeResponse   code =="+ billingResult.getResponseCode() +
                        " ,  msg = " + billingResult.getDebugMessage() +
                        " , purchaseToken = " + purchaseToken;


                Log.e(TAG, onConsumeResponse);

                if (isNeedAddDiamond){
                    addDiamond(purchase);
                    mPayCallBack.paySuccess();
                }else {
                }
            }
        });
    }

    private void addDiamond(final Purchase purchase) {
        HttpMethods.getInstance().
                gPay(purchase.getOriginalJson(),purchase.getSignature(),
                        mProductBean.getProductIosId()+"", Constants.DEBUG_MODE_FOR_MONEY?3:1,
                        new HttpObserver(new SubscriberOnNextListener() {
                            @Override
                            public void onNext(Object o, String msg) {
                                UserBean myInfo = DataManager.getInstance().getMyUserInfo();
                                myInfo.setDiamond(myInfo.getDiamond() + mProductBean.getDiamond());
                                DataManager.getInstance().saveMyUserInfo(myInfo);
                                ChatterToast.Toast(mContext.getResources().getString(R.string.chatter_buy_diamond_success));
//                                hideLoadingDialog();
                            }
                        }, mContext, new OnHttpErrorListener() {
                            @Override
                            public void onConnectError(Throwable e) {
//                                hideLoadingDialog();
                            }

                            @Override
                            public void onServerError(int errorCode, String errorMsg) {
//                                hideLoadingDialog();
                            }
                        }));
    }




}
