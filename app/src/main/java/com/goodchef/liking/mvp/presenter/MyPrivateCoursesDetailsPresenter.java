package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;
import com.goodchef.liking.module.data.local.Preference;

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
        LiKingApi.getMyPrivateCoursesDetails(Preference.getToken(), orderId, new RequestCallback<MyPrivateCoursesDetailsResult>() {
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
        LiKingApi.completerMyPrivateCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateComplete();
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
