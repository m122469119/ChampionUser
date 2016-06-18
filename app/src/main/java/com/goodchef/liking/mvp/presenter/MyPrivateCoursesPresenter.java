package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderViewFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 下午2:58
 */
public class MyPrivateCoursesPresenter extends BasePresenter<MyPrivateCoursesView> {

    public MyPrivateCoursesPresenter(Context context, MyPrivateCoursesView mainView) {
        super(context, mainView);
    }

    public void getMyPrivateCourses(int page, BasePagerLoaderViewFragment fragment) {
        LiKingApi.getMyPrivateList(Preference.getToken(), page, new BasePagerLoaderViewFragment.PagerRequestCallback<MyPrivateCoursesResult>(fragment) {
            @Override
            public void onSuccess(MyPrivateCoursesResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updatePrivateCoursesView(result.getData());
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
