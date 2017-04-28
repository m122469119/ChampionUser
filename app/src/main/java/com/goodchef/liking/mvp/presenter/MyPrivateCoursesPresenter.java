package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 下午2:58
 */
public class MyPrivateCoursesPresenter extends BasePresenter<MyPrivateCoursesView> {

    public MyPrivateCoursesPresenter(Context context, MyPrivateCoursesView mainView) {
        super(context, mainView);
    }

    public void getMyPrivateCourses(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getMyPrivateList(Preference.getToken(), page, new PagerRequestCallback<MyPrivateCoursesResult>(fragment) {
            @Override
            public void onSuccess(MyPrivateCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesView(result.getData());
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
