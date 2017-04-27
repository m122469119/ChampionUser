package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.CardResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午12:29
 */
public interface CardListView extends BaseNetworkLoadView {
    void updateCardListView(CardResult.CardData cardData);
}
