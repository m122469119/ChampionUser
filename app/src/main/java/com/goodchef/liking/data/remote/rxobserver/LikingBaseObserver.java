package com.goodchef.liking.data.remote.rxobserver;

import android.net.ParseException;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.ConstantUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.LikingBaseRequestHelper;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.eventmessages.LoginInvalidMessage;
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

public abstract class LikingBaseObserver<T extends LikingResult> extends BaseRequestObserver<T> {
    private BaseView mView;

    public static final int IS_START_HOME = 100;
    public LikingBaseObserver(BaseView view) {
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
                    LoginInvalidMessage message = new LoginInvalidMessage();
                    if (!ConstantUtils.BLANK_STRING.equals(LikingPreference.getToken())) {
                        message.arg1 = IS_START_HOME;
                    }
                    LikingPreference.setToken(ConstantUtils.BLANK_STRING);
                    LikingPreference.setNickName(ConstantUtils.BLANK_STRING);
                    LikingPreference.setUserPhone(ConstantUtils.BLANK_STRING);
                    LikingPreference.setIsNewUser(null);
                    LikingPreference.setUserIconUrl(ConstantUtils.BLANK_STRING);
                    EventBus.getDefault().post(message);
                    break;
                case LiKingRequestCode.REQEUST_TIMEOUT:
                    LikingBaseRequestHelper.isTimestampInit = false;
                    LikingBaseRequestHelper.isBaseConfigInit = false;
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

    public BaseView getView() {
        return mView;
    }

    public void networkError(Throwable throwable) {
        LogUtils.e(TAG, throwable.toString());
        mView.showToast(R.string.network_error);
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            doHttpException(httpException);
        }
    }

    /**
     * 处理网络错误上报日志
     */
    private void doHttpException(HttpException httpException) {
        if (httpException.code() == 500) {
            String errorStr = httpException.response().errorBody().toString();
            String head = httpException.response().headers().toString();
            LogUtils.i(TAG, "errorBody =  " + errorStr + " headers= " + head);
            LikingNewApi.getInstance().uploadNetworkError(LikingNewApi.sHostVersion, head, errorStr).compose(RxUtils.<LikingResult>applyHttpSchedulers())
                    .subscribe(new BaseRequestObserver<LikingResult>() {
                        @Override
                        public void onNext(LikingResult value) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            HttpException httpException = (HttpException) e;
                            if (httpException.code() != 0) {
                                return;
                            }
                        }
                    });
        }
    }

    public void apiError(ApiException apiException) {
        LogUtils.e(TAG, apiException.toString());
        mView.showToast(apiException.getErrorMessage());
    }
}
