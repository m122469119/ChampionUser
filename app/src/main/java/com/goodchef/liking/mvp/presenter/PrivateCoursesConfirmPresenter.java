package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午11:21
 */
public class PrivateCoursesConfirmPresenter extends BasePresenter<PrivateCoursesConfirmView> {
    public PrivateCoursesConfirmPresenter(Context context, PrivateCoursesConfirmView mainView) {
        super(context, mainView);
    }

    public void orderPrivateCoursesConfirm(String gymId, String trainerId) {
        LiKingApi.orderPrivateCoursesConfirm(gymId, trainerId, Preference.getToken(), new RequestCallback<PrivateCoursesConfirmResult>() {
            @Override
            public void onSuccess(PrivateCoursesConfirmResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesConfirm(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }

    public void submitPrivateCourses(String courseId, String couponCode, String payType, int selectTimes, String gymId) {
        LiKingApi.submitPrivateCourses(Preference.getToken(), courseId, couponCode, payType, selectTimes, gymId, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSubmitOrderCourses(result.getPayData());
                } else if (result.getCode() != 221009) {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    public void orderCalculate(String courseId, String selectTimes) {
        LiKingApi.orderCalculate(Preference.getToken(), courseId, selectTimes, new RequestCallback<OrderCalculateResult>() {
            @Override
            public void onSuccess(OrderCalculateResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateOrderCalculate(true, result.getData());
                } else {
                    mView.updateOrderCalculate(false, result.getData());
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.updateOrderCalculate(false, null);
            }
        });
    }
}
