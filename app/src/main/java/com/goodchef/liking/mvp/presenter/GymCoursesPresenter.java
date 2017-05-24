package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GymCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.GymCoursesView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/3 下午8:19
 */
public class GymCoursesPresenter extends BasePresenter<GymCoursesView> {
    public GymCoursesPresenter(Context context, GymCoursesView mainView) {
        super(context, mainView);
    }

    public void getGymCoursesList(String gymId, String date) {
        LiKingApi.getGymCoursesList(gymId, date, new RequestUiLoadingCallback<GymCoursesResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(GymCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateGymCoursesView(result.getData());
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
