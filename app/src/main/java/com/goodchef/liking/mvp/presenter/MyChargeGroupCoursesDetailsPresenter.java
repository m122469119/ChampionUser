package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyChargeGroupCoursesDetailsView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/22 下午3:58
 */
public class MyChargeGroupCoursesDetailsPresenter extends BasePresenter<MyChargeGroupCoursesDetailsView> {
    public MyChargeGroupCoursesDetailsPresenter(Context context, MyChargeGroupCoursesDetailsView mainView) {
        super(context, mainView);
    }

    public void getMyGroupDetails(String orderId) {
        LiKingApi.getMyChargeGroupCoursesDetails(Preference.getToken(), orderId, new RequestCallback<MyChargeGroupCoursesDetailsResult>() {
            @Override
            public void onSuccess(MyChargeGroupCoursesDetailsResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyChargeGroupCoursesDetailsView(result.getData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }
}
