package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.data.NoticeData;

import java.util.Set;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface LikingHomeView extends BaseView {

    void showNoticesDialog(Set<NoticeData> noticeDatas);
}
