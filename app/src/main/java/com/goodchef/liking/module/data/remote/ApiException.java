package com.goodchef.liking.module.data.remote;

/**
 * Created on 17/5/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class ApiException extends RuntimeException {
    private int mErrorCode;
    private String mErrorMessage;

    public ApiException(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
