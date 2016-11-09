package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.SelfHelpGroupCoursesView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author : liking
 * Time: 下午3:46
 */

public class SelfHelpGroupCoursesPresenter extends BasePresenter<SelfHelpGroupCoursesView> {

    public SelfHelpGroupCoursesPresenter(Context context, SelfHelpGroupCoursesView mainView) {
        super(context, mainView);
    }

    public void getSelfHelpCourses(String gymId) {
        LiKingApi.getSelfCorsesTimeList(Preference.getToken(), gymId, new RequestCallback<SelfHelpGroupCoursesResult>() {
            @Override
            public void onSuccess(SelfHelpGroupCoursesResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSelfHelpGroupCoursesView(result.getData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
