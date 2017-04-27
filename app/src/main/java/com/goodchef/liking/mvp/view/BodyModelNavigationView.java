package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.BodyModelNavigationResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午12:15
 * version 1.0.0
 */

public interface BodyModelNavigationView extends BaseNetworkLoadView{
    void  updateBodyModelNavigationView(BodyModelNavigationResult.HistoryTitleData data);
}
