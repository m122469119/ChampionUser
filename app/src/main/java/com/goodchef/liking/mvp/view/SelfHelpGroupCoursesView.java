package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;

/**
 * 说明:
 * Author : liking
 * Time: 下午3:16
 */

public interface SelfHelpGroupCoursesView extends BaseNetworkLoadView {
    void updateSelfHelpGroupCoursesView(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData selfHelpGroupCoursesData);

    void updateOrderView();
    void updateNoCardView(String message);
    void updateSelectCourserView();
}
