package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyUserOtherInfoResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:56
 * version 1.0.0
 */

public interface MyUserInfoOtherView extends BaseNetworkLoadView {
    void updateMyUserInfoOtherView(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData);
}
