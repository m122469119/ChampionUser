package com.goodchef.liking.module.base;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingBaseRequestObserver<T extends BaseResult> implements Observer<T> {
    private static final String TAG = "LikingBaseRequestObserver";
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
