package com.goodchef.liking.module.data.remote.rxobserver;

import android.content.Context;
import android.net.ParseException;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.ConstantUtils;
import com.aaron.http.code.result.Result;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginInvalidMessage;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.ApiException;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.net.ConnectException;

import de.greenrobot.event.EventBus;
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

    public LikingBaseObserver(Context context, BaseView view) {
        mContext = context;
        mView = view;
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
        if (e == null) {
            return;
        }
        if (e instanceof ApiException) {
            switch (((ApiException) e).getErrorCode()) {
                case LiKingRequestCode.LOGIN_TOKEN_INVALID:
                    LikingPreference.setToken(ConstantUtils.BLANK_STRING);
                    LikingPreference.setNickName(ConstantUtils.BLANK_STRING);
                    LikingPreference.setUserPhone(ConstantUtils.BLANK_STRING);
                    LikingPreference.setIsNewUser(null);
                    LikingPreference.setUserIconUrl(ConstantUtils.BLANK_STRING);
                    EventBus.getDefault().post(new LoginInvalidMessage());
                    break;
                case LiKingRequestCode.REQEUST_TIMEOUT:
//                    initApi(context);
                    break;
                case LiKingRequestCode.ILLEGAL_REQUEST:
                case LiKingRequestCode.ILLEGAL_ARGUMENT:
                case LiKingRequestCode.ARGUMENT_HIATUS_EXCEPTION:
                case LiKingRequestCode.DB_CONNECTION_EXCEPTION:
                case LiKingRequestCode.REDIS_CONNECTION_EXCEPTION:
                case LiKingRequestCode.SERVER_EXCEPTION:
                    networkError(e);
                    break;
                default:
                    apiError((ApiException) e);
                    break;
            }
        } else if (e instanceof HttpException
                || e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof ConnectException) {
            //网络异常
            networkError(e);
        } else {
            //未知错误
            networkError(e);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public BaseView getView() {
        return mView;
    }

    public void networkError(Throwable throwable) {
        mView.showToast(mContext.getString(R.string.network_error));
    }

    public void apiError(ApiException apiException) {
        mView.showToast(apiException.getErrorMessage());
    }
}
