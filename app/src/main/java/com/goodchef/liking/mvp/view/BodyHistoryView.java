package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.BodyHistoryResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:30
 * version 1.0.0
 */

public interface BodyHistoryView extends BaseNetworkLoadView {
    void updateBodyHistoryView(BodyHistoryResult.BodyHistoryData data);
}
