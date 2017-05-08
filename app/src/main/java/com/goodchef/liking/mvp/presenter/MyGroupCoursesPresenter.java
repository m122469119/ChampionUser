package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyGroupCourseView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 上午11:52
 */
public class MyGroupCoursesPresenter extends BasePresenter<MyGroupCourseView> {
    public MyGroupCoursesPresenter(Context context, MyGroupCourseView mainView) {
        super(context, mainView);
    }

    public void getMyGroupList(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getMyGroupList(Preference.getToken(), page, new PagerRequestCallback<MyGroupCoursesResult>(fragment) {
            @Override
            public void onSuccess(MyGroupCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyGroupCoursesView(result.getData());
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

    public void sendCancelCoursesRequest(String orderId){
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    PopupUtils.showToast(mContext, R.string.cancel_success);
                    mView.updateLoadHomePage();
                } else {
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
