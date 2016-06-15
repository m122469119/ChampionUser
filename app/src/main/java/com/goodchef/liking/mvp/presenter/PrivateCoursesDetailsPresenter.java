package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.PrivateCoursesDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/12 下午6:35
 */
public class PrivateCoursesDetailsPresenter extends BasePresenter<PrivateCoursesDetailsView> {

    public PrivateCoursesDetailsPresenter(Context context, PrivateCoursesDetailsView mainView) {
        super(context, mainView);
    }

    public void getPrivateCoursesDetails(String trainerId) {
        LiKingApi.getPrivateCoursesDetails(trainerId, new RequestUiLoadingCallback<PrivateCoursesResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(PrivateCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesDetailsView(result.getPrivateCoursesData());
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
