package com.goodchef.liking.module.card;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.MyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.MyOrderCardDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.OrderCardListResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterDetailsResult;

import io.reactivex.Observable;

/**
 * Created on 2017/04/25
 * desc: 会员购卡、购卡订单
 *
 * @author: chenlei
 * @version:1.0
 */

public class CardModel extends BaseModel {

    /**
     * 获取我的会员卡
     *
     * @return
     */
    public Observable<MyCardResult> getMyCard() {
        return LikingNewApi.getInstance().getMyCard(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<MyCardResult>applyHttpSchedulers());
    }

    /**
     * 我的会员卡订单详情
     *
     * @param orderId
     * @return
     */
    public Observable<MyOrderCardDetailsResult> getMyOrderCardDetails(String orderId) {
        return LikingNewApi.getInstance().getMyOrderCardDetails(LikingNewApi.sHostVersion,
                LikingPreference.getToken(),
                orderId)
                .compose(RxUtils.<MyOrderCardDetailsResult>applyHttpSchedulers());
    }

    public Observable<WaterDetailsResult> getWaterDetails(String orderId) {
        return LikingNewApi.getInstance().getWaterDetailsResult(LikingNewApi.sHostVersion,
                LikingPreference.getToken(),
                orderId)
                .compose(RxUtils.<WaterDetailsResult>applyHttpSchedulers());
    }

    /***
     * 获取我的会员卡订单列表
     *
     * @param page     页数
     * @return
     */
    public Observable<OrderCardListResult> getCardOrderList(int page) {
        return LikingNewApi.getInstance().getCardOrderList(LikingNewApi.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<OrderCardListResult>applyHttpSchedulers());
    }


}
