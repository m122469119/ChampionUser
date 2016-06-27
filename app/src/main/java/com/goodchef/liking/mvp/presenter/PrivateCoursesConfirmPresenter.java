package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午11:21
 */
public class PrivateCoursesConfirmPresenter extends BasePresenter<PrivateCoursesConfirmView> {
    public PrivateCoursesConfirmPresenter(Context context, PrivateCoursesConfirmView mainView) {
        super(context, mainView);
    }

    public void orderPrivateCoursesConfirm(String trainerId) {
        LiKingApi.orderPrivateCoursesConfirm(trainerId, Preference.getToken(), new RequestUiLoadingCallback<PrivateCoursesConfirmResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(PrivateCoursesConfirmResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesConfirm(result.getData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    public void submitPrivateCourses(String courseId, String couponCode, String payType) {
        LiKingApi.submitPrivateCourses(Preference.getToken(), courseId, couponCode, payType, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSubmitOrderCourses(result.getPayData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
