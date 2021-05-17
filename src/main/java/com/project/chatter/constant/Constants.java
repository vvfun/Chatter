package com.project.chatter.constant;

/**
 * Created by gigabud on 15-12-4.
 */
public class Constants {

    // 是否测试模式
    public static final boolean DEBUG_MODE = false;
    public static final boolean DEBUG_MODE_LOG = true;
    public static final boolean DEBUG_MODE_FOR_MONEY = false;//TODO test /*！！！！！！重要*/

    public static final String BASE_URL = Constants.DEBUG_MODE ?  "https://t.livego.live/" : "https://api.livehub.cloud/";

    public static final String KEY_BASE_BEAN = "key_base_bean";
    public static final String KEY_BASE_BEAN_1 = "key_base_bean_1";
    public static final String KEY_BASE_BEAN_2 = "key_base_bean_2";
    public static final String KEY_MODE = "key_mode";


    public static final int ONE_PAGE_SIZE = 20;


}