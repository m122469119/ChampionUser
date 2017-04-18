package com.goodchef.liking.module.base;

import android.app.ProgressDialog;
import android.content.Context;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.utils.ResourceUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class ProgressObserver<T extends BaseResult> extends LikingBaseRequestObserver<T> {
    private static final String TAG = "ProgressObserver";
    private ProgressDialog mProgressDialog;


    public ProgressObserver(Context context, String msg) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
    }

    public ProgressObserver(Context context, int resId) {
        this(context, ResourceUtils.getString(resId));
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        showProgress();
    }

    @Override
    public void onNext(T result) {
        super.onNext(result);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissProgress();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissProgress();
    }

    private void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


}
