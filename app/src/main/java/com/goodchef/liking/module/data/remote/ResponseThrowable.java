package com.goodchef.liking.module.data.remote;

import com.aaron.http.code.RequestError;

/**
 * Created on 17/5/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class ResponseThrowable extends Exception {

    private RequestError.ErrorType mErrorType;
    private String mMessage;

    public ResponseThrowable(RequestError.ErrorType errorType, Throwable throwable) {
        super(throwable);
        mErrorType = errorType;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public RequestError.ErrorType getErrorType() {
        return mErrorType;
    }

    public Throwable getTrowable() {
        return getCause();
    }
}
