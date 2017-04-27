package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 下午2:55
 */
public interface MyPrivateCoursesView extends BaseView {
    void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData);
}
