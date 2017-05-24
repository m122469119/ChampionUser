package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.SelfHelpGroupCoursesView;
import com.goodchef.liking.module.data.local.LikingPreference;

/**
 * 说明:
 * Author : liking
 * Time: 下午3:46
 */

public class SelfHelpGroupCoursesPresenter extends BasePresenter<SelfHelpGroupCoursesView> {

    public SelfHelpGroupCoursesPresenter(Context context, SelfHelpGroupCoursesView mainView) {
        super(context, mainView);
    }

    public void getSelfHelpCourses(String gymId) {
        LiKingApi.getSelfCorsesTimeList(LikingPreference.getToken(), gymId, new RequestCallback<SelfHelpGroupCoursesResult>() {
            @Override
            public void onSuccess(SelfHelpGroupCoursesResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSelfHelpGroupCoursesView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }

    public void sendOrderRequest(String gymId, String roomId, String coursesId, String coursesDate, String startTime, String endTime, String price, String peopleNum) {
        LiKingApi.orderCourses(LikingPreference.getToken(), gymId, roomId, coursesId, coursesDate, startTime, endTime, price, peopleNum, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateOrderView();
                } else if (likingResult.getCode() == 22013) {
                   mView.updateNoCardView(likingResult.getMessage());
                } else {
                    mView.updateSelectCourserView();//刷新选中的View(当前时刻-房间被其他人预约,后台返回码不唯一,刷新接口后刷新选中view)
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }



}
