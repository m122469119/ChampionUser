package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.SelectCoursesListView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:43
 */

public class SelectCoursesListPresenter extends BasePresenter<SelectCoursesListView> {
    public SelectCoursesListPresenter(Context context, SelectCoursesListView mainView) {
        super(context, mainView);
    }

    public void getCoursesList(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getSelfCoursesList(page, new PagerRequestCallback<SelfGroupCoursesListResult>(fragment) {
            @Override
            public void onSuccess(SelfGroupCoursesListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSelectCoursesListView(result.getData());
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
