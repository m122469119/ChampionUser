package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.CoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/8 下午3:26
 */
public interface HomeCourseView extends BaseView {
    void updateCourseView(CoursesResult.Courses courses);
}
