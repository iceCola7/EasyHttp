package com.cxz.sample.rxeasyhttp.api;

/**
 * Created by chenxz on 2018/7/1.
 */

public class ApiResponse<T> {

    private String errorCode;
    private String errorMsg;
    private T data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("errorCode=" + errorCode + " errorMsg=" + errorMsg);
        if (null != data) {
            sb.append(" result:" + data.toString());
        }
        return sb.toString();
    }

}
