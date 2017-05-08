package com.goodchef.liking.http.callback;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.widgets.dialog.LiKingLoadingDialog;


/**
 * Created by Lennon on 15/6/18.
 */
public abstract class RequestUiLoadingCallback<T extends LikingResult> extends RequestCallback<T> {
    private LiKingLoadingDialog mLoadingDialog;

    public RequestUiLoadingCallback(Context ctx, String msg, boolean cancelable) {
        mLoadingDialog = new LiKingLoadingDialog(ctx);
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(cancelable);
    }

    public RequestUiLoadingCallback(Context ctx, int msg, boolean cancelable) {
        this(ctx, ctx.getString(msg), cancelable);
    }

    public RequestUiLoadingCallback(Context context, String msg) {
        this(context, msg, false);
    }

    public RequestUiLoadingCallback(Context context, int msg) {
        this(context, msg, false);
    }
    @Override
    public void onStart() {
        showLoading();
    }

    @Override
    public void onSuccess(T result) {
        dismissLoading();
    }

    @Override
    public void onFailure(RequestError error) {
        dismissLoading();
    }


    public final void showLoading(){
        if(mLoadingDialog != null && !mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }
    }

    public final void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
