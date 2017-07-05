package com.goodchef.liking.module.card.buy.confirm;

import android.text.TextUtils;

import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.module.home.LikingHomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created on 2017/7/4
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class BuyCardConfirmModel {
    public static final int BUY_TYPE_BUY = 1;//买卡
    public static final int BUY_TYPE_CONTINUE = 2;//续卡
    public static final int BUY_TYPE_UPGRADE = 3;//升级


    public String mCardName = "";
    public String mCategoryId = "";
    public int mBuyType; //1 购卡  2 续卡  3 升级卡
    public String mGymId = "0";
    public String mCardId = "";

    public String mSubmitGymId = "";

    List<ConfirmBuyCardResult.DataBean.CardsBean.TimeLimitBean> mTimeLimitBeanList = new ArrayList<>();

    /***
     * 卡详情
     *
     */
    public Observable<ConfirmBuyCardResult> confirmCard() {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(mBuyType));
        map.put("category_id", String.valueOf(mCategoryId));
        map.put("gym_id", mGymId);
        map.put("card_id", String.valueOf(mCardId));
        map.put("token", LikingPreference.getToken());
        return LikingNewApi.getInstance().cardConfirm(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<ConfirmBuyCardResult>applyHttpSchedulers())
                .doOnNext(new Consumer<ConfirmBuyCardResult>() {
                    @Override
                    public void accept(ConfirmBuyCardResult confirmBuyCardResult) throws Exception {
                        mTimeLimitBeanList.clear();
                        mTimeLimitBeanList.addAll(confirmBuyCardResult.getData().getCards().get(0).getTime_limit());
                    }
                })
                ;
    }


    /***
     * 提交买卡订单
     *
     * @param couponCode 优惠券code
     * @param payType    支付方式
     */
    public Observable<SubmitPayResult> submitBuyCardData(String couponCode, String payType) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("card_id", String.valueOf(mCardId));
        map.put("type", String.valueOf(mBuyType));
        map.put("coupon_code", couponCode);
        map.put("pay_type", payType);
        map.put("gym_id", mGymId);
        return LikingNewApi.getInstance().submitBuyCardData(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<SubmitPayResult>applyHttpSchedulers());
    }

    public void setIntentParams(String cardName, String categoryId, int buyType, String gymId, String cardId) {
        mCardName = cardName;
        mCategoryId = categoryId;
        mBuyType = buyType;
        mGymId = gymId;
        mCardId = cardId;
        setGymId();
    }


    private void setGymId() {
        if (mBuyType == BUY_TYPE_BUY) {//买卡
            mSubmitGymId = LikingHomeActivity.gymId;
        } else if (mBuyType == BUY_TYPE_CONTINUE) {//续卡
            mSubmitGymId = mGymId;
        } else if (mBuyType == BUY_TYPE_UPGRADE) {//升级卡
            mSubmitGymId = mGymId;
        }
    }


}
