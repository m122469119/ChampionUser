package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.ChargeGroupCoursesView;
import com.goodchef.liking.storage.Preference;

import de.greenrobot.event.EventBus;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 上午10:22
 */
public class ChargeGroupCoursesConfirmPresenter extends BasePresenter<ChargeGroupCoursesView> {

    public ChargeGroupCoursesConfirmPresenter(Context context, ChargeGroupCoursesView mainView) {
        super(context, mainView);
    }

    public void getChargeGroupCoursesConfirmData(String gymId, String scheduleId) {
        LiKingApi.chargeGroupCoursesConfirm(Preference.getToken(), gymId, scheduleId, new RequestCallback<ChargeGroupConfirmResult>() {
            @Override
            public void onSuccess(ChargeGroupConfirmResult result) {
                if (result.getCode() == 0) {
                    mView.updateChargeGroupCoursesView(result.getData());
                } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_ERROR) {
                    mView.updateBuyCoursesNotOnGym(result.getMessage());
                } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_NO_CARD) {
                    mView.updateErrorNoCard(result.getMessage());
                } else if (result.getCode() == LiKingRequestCode.LOGIN_TOKEN_INVALID) {
                    mView.updateBuyCoursesErrorView();
                    mView.showToast(result.getMessage());
                    EventBus.getDefault().post(new LoginOutFialureMessage());
                } else {
                    mView.showToast(result.getMessage());
                    mView.updateBuyCoursesErrorView();
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }

    public void chargeGroupCoursesImmediately(String gymId, String scheduleId, String couponCode, String payType) {
        LiKingApi.chargeGroupCoursesImmediately(Preference.getToken(), gymId, scheduleId, couponCode, payType, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePaySubmitView(result.getPayData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
