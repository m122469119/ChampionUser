package com.goodchef.liking.module.data.remote.rxobserver;

import android.content.Context;

import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.http.code.RequestError;
import com.aaron.http.code.result.Result;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.ResponseThrowable;

import io.reactivex.disposables.Disposable;

/**
 * Created on 17/5/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class LikingBaseObserver<T extends Result> extends BaseRequestObserver<T> {

    private Context mContext;

    public LikingBaseObserver(Context context) {
        mContext = context;
    }

    public abstract void onError(ResponseThrowable responseThrowable);

    public void onStart() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        onStart();
    }

    @Override
    public void onError(Throwable e) {
        ResponseThrowable responseThrowable;
        if (e instanceof ResponseThrowable) {
            responseThrowable = (ResponseThrowable) e;
        } else {
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.UNKNOWN, e);
        }
        onError(responseThrowable);
        if (responseThrowable.getTrowable() != null
                && responseThrowable.getTrowable() instanceof ApiException) {
            PopupUtils.showToast(mContext, ((ApiException) responseThrowable.getTrowable()).getErrorMessage());
        }
    }

    public Context getContext() {
        return mContext;
    }

}
