package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.UserAuthCodeResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/14 下午5:07
 */
public interface UserAuthView extends BaseView {
    void updateUserAuthView(UserAuthCodeResult.UserAuthCodeData userAuthCodeData);
    void updateFailCodeView(String message);
}
