package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CoursesDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/12 下午6:35
 */
public class CoursesDetailsPresenter extends BasePresenter<CoursesDetailsView> {

    public CoursesDetailsPresenter(Context context, CoursesDetailsView mainView) {
        super(context, mainView);
    }

    public void getGroupCoursesDetails(String scheduleId) {
        LiKingApi.getGroupLessonDetails(scheduleId, new RequestUiLoadingCallback<GroupCoursesResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(GroupCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateGroupLessonDetailsView(result.getGroupLessonData());
                }else {
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
