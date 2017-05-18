package com.goodchef.liking.module.data.remote.rxobserver;

import android.content.Context;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.http.code.RequestError;
import com.aaron.http.code.result.Result;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.R;
import com.goodchef.liking.http.verify.LiKingRequestCode;
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

    private BaseView mView;

    public LikingBaseObserver(Context mContext, BaseView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    public void onError(ResponseThrowable responseThrowable) {
        switch (responseThrowable.getErrorType()) {
            case HTTP_ERROR:
            case NETWORK_ERROR:
            case UNKNOWN:
            case PARSE_ERROR:
                defaultErrorUIShow(mContext.getString(R.string.network_error));
                break;
            case SERVER_ERROR:
                ApiException apiException = (ApiException) responseThrowable.getTrowable();
                switch (apiException.getErrorCode()) {
                    case LiKingRequestCode.REQEUST_TIMEOUT:
                        //                    initApi(context);
                        break;
                    case LiKingRequestCode.ILLEGAL_REQUEST:
                    case LiKingRequestCode.ILLEGAL_ARGUMENT:
                    case LiKingRequestCode.ARGUMENT_HIATUS_EXCEPTION:
                    case LiKingRequestCode.DB_CONNECTION_EXCEPTION:
                    case LiKingRequestCode.REDIS_CONNECTION_EXCEPTION:
                    case LiKingRequestCode.SERVER_EXCEPTION:
                        defaultErrorUIShow("服务器异常");
                        break;
                    default:
                        defaultErrorUIShow(apiException.getErrorMessage());
                        break;
                }
                break;
        }
    }

    public void defaultErrorUIShow(String s) {
        mView.showToast(s);
    }


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
    }

    public Context getContext() {
        return mContext;
    }

}
