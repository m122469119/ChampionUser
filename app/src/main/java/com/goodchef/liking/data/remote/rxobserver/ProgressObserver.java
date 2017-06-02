package com.goodchef.liking.data.remote.rxobserver;

import android.app.ProgressDialog;
import android.content.Context;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

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


    public ProgressObserver(Context context, String msg, BaseView view) {
        super(view);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
    }

    public ProgressObserver(Context context, int resId, BaseView view) {
        this(context, ResourceUtils.getString(resId), view);
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
