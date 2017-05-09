package com.aaron.http.rxobserver;

import com.aaron.http.code.result.Result;
import com.aaron.common.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BaseRequestObserver<T extends Result> implements Observer<T> {
    protected final String TAG = getClass().getName();
    private Disposable mDisposable;
    @Override
    public void onSubscribe(Disposable d) {
        LogUtils.d(TAG, "ProgressObserver onSubscribe...");
        mDisposable = d;
    }

    @Override
    public void onNext(T result) {
        LogUtils.d(TAG, "ProgressObserver onNext...");
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.d(TAG, "ProgressObserver onError...");
    }

    @Override
    public void onComplete() {
        LogUtils.d(TAG, "ProgressObserver onComplete...");
    }

    public Disposable getDisposable() {
        return mDisposable;
    }
}
