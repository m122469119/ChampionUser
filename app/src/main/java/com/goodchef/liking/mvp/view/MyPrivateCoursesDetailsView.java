package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/20 下午7:20
 */
public interface MyPrivateCoursesDetailsView extends BaseNetworkLoadView {
    void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData myPrivateCoursesDetailsData);
    void updateComplete();
}
