package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.CheckUpdateAppResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:56
 * version 1.0.0
 */

public interface CheckUpdateAppView extends BaseView {
    void updateCheckUpdateAppView(CheckUpdateAppResult.UpdateAppData updateAppData);
}
