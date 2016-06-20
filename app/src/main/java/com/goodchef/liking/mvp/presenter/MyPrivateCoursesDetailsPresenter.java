package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;
import com.goodchef.liking.storage.Preference;

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
        LiKingApi.getMyPrivateCoursesDetails(Preference.getToken(), orderId, new RequestUiLoadingCallback<MyPrivateCoursesDetailsResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(MyPrivateCoursesDetailsResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyPrivateCoursesDetailsView(result.getData());
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
