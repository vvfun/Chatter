package com.project.chatter.data;

import com.project.chatter.bean.ProductBean;
import com.project.chatter.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by gigabud on 17-12-14.
 */

public interface IDataManager {


    public void clearMyUserInfo();

    public void setObject(Object object);

    public Object getObject();
    public void saveMyUserInfo(UserBean myUserInfo);

    public UserBean getMyUserInfo();

    void setAgoId(String agoId);

    String getAgoId();
}
