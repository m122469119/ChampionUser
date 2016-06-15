package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.PrivateCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/12 下午6:28
 */
public interface PrivateCoursesDetailsView extends BaseView {
    void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData);
}
