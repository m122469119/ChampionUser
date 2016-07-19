package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyCardResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午4:40
 */
public interface MyCardView extends BaseNetworkLoadView {
    void updateMyCardView(MyCardResult.MyCardData myCardData);
}
