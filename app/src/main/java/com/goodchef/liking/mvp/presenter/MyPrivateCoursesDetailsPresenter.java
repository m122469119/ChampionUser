package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;
import com.goodchef.liking.module.data.local.LikingPreference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/20 下午7:22
 */
public class MyPrivateCoursesDetailsPresenter extends BasePresenter<MyPrivateCoursesDetailsView> {
    public MyPrivateCoursesDetailsPresenter(Context context, MyPrivateCoursesDetailsView mainView) {
        super(context, mainView);
    }

    public void getMyPrivateCoursesDetails(String orderId) {
        LiKingApi.getMyPrivateCoursesDetails(LikingPreference.getToken(), orderId, new RequestCallback<MyPrivateCoursesDetailsResult>() {
            @Override
            public void onSuccess(MyPrivateCoursesDetailsResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyPrivateCoursesDetailsView(result.getData());
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

    public void completeMyPrivateCourses(String orderId) {
        LiKingApi.completerMyPrivateCourses(LikingPreference.getToken(), orderId, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateComplete();
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
