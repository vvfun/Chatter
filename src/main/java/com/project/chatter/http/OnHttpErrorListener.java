package com.project.chatter.http;

/**
 * Created by gigabud on 17-5-4.
 */

public interface OnHttpErrorListener {
    void onConnectError(Throwable e);
    void onServerError(int errorCode, String errorMsg);
}
