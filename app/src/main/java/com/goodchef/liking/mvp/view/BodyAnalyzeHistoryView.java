package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:00
 * version 1.0.0
 */

public interface BodyAnalyzeHistoryView extends BaseNetworkLoadView {
    void updateBodyAnalyzeHistoryView(BodyAnalyzeHistoryResult.BodyHistory data);
}
