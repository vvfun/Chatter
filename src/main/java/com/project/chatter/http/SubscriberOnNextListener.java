package com.project.chatter.http;

public interface SubscriberOnNextListener<T> {
    void onNext(T t, String msg);
}
