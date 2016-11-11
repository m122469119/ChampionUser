package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:41
 */

public interface SelectCoursesListView extends BaseNetworkLoadView {
    void updateSelectCoursesListView(SelfGroupCoursesListResult.SelfGroupCoursesData data);
}
