package com.iapppay.channel.pack.interfaces;

/**
 * 结果回调
 */

public interface ResultCallback<T> {
    void onSuccess(T data);

    void onError(String errorMsg);
}
