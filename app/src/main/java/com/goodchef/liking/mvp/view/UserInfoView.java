package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.UserInfoResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/4 下午11:03
 */
public interface UserInfoView extends BaseView {
    void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData);
    void updateUserInfo();
}
