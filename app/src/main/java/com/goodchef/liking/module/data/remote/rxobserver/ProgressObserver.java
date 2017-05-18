package com.goodchef.liking.module.data.remote.rxobserver;

import android.app.ProgressDialog;
import android.content.Context;

import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.http.result.LikingResult;

import io.reactivex.disposables.Disposable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class ProgressObserver<T extends LikingResult> extends LikingBaseObserver<T> {
    private static final String TAG = "ProgressObserver";
    private ProgressDialog mProgressDialog;


    public ProgressObserver(Context context, String msg) {
        super(context, null);
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
    public void onError(Throwable e) {
        super.onError(e);
        dismissProgress();
    }

    @Override
    public void onComplete() {
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
