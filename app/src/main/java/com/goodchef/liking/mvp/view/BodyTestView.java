package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.BodyTestResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:24
 * version 1.0.0
 */

public interface BodyTestView extends BaseNetworkLoadView {
    void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData);
}
