package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.HomeCourseView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/8 下午3:30
 */
public class HomeCoursesPresenter extends BasePresenter<HomeCourseView> {
    public HomeCoursesPresenter(Context context, HomeCourseView mainView) {
        super(context, mainView);
    }

    public void getHomeData(double longitude, double latitude, String cityId, String districtId, int currentPage) {
        LiKingApi.getHomeData(longitude, latitude, cityId, districtId, currentPage, new RequestUiLoadingCallback<CoursesResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(CoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCourseView(result.getCourses());
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
