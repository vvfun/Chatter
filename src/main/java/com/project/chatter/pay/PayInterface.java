package com.project.chatter.pay;

import android.app.Activity;
import android.content.Intent;

import com.project.chatter.bean.ProductBean;


public interface PayInterface {

    public void init(Activity activity);

    public void Pay(ProductBean productBean);

    public void setCallBack(PayCallBack.inter callBack);

    public void onReturnResult(int requestCode, int resultCode, Intent data);


}
