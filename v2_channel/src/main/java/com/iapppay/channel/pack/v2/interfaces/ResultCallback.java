package com.iapppay.channel.pack.v2.interfaces;

/**
 * 结果回调
 */

public interface ResultCallback<T> {
    void onSuccess(T data);

    void onError(String errorMsg);
}
