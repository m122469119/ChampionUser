package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.GroupCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/15 下午5:27
 */
public interface GroupCourserDetailsView extends BaseNetworkLoadView {
    void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData);
    void updateOrderGroupCourses();
    void updateErrorNoCard(String errorMessage);
    void updateCancelOrderView();
}
