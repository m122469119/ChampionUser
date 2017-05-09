package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
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
        LiKingApi.getPrivateCoursesDetails(trainerId, new RequestCallback<PrivateCoursesResult>() {
            @Override
            public void onSuccess(PrivateCoursesResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesDetailsView(result.getPrivateCoursesData());
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
}
