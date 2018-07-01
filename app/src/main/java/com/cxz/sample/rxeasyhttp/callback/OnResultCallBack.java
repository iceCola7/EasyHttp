package com.cxz.sample.rxeasyhttp.callback;

/**
 * Created by chenxz on 2018/7/1.
 */

public interface OnResultCallBack<T> {

    void onSuccess(T t);

    void onError(int code, String errorMsg);

}
