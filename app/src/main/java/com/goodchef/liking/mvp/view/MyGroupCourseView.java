package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.MyGroupCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 上午11:50
 */
public interface MyGroupCourseView extends BaseView {
    void updateMyGroupCoursesView(MyGroupCoursesResult.MyGroupCoursesData myGroupCoursesData);
    void updateLoadHomePage();
}
