package com.project.chatter.pay;

public interface PayCallBack {
    interface out{
        void paySuccess();
    }
    interface inter{
        void paySuccess();
        void cancel();
    }
}
