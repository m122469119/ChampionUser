package com.goodchef.liking.module.card.buy;

import android.text.TextUtils;

import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.retrofit.result.CardResult.CardData;
import com.goodchef.liking.data.remote.retrofit.result.data.GymData;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created on 2017/7/3
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class BuyCardModel {

    private String longitude = "0";
    private String latitude = "0";
    private String cityId = "310100";
    private String districtId = "310104";
    private GymData mGymData;

    //1只有错峰  2只有全通  3既有错峰又有全通 4既无错峰又无全通
    private int mCardOnlyType = -1;

    public int mCheckedType = -1;
    public boolean isActivity = false;

    private List<CardData.Category.CardBean> mCardAllList = new ArrayList<>();
    private List<CardData.Category.CardBean> mCardStaggerList = new ArrayList<>();
    private CardResult mCardResult;

    public List<CardData.Category.CardBean> getCardList() {

        if (mCardOnlyType == CardData.ALL_CARD) {
            return mCardAllList;
        } else if (mCardOnlyType == CardData.STAGGER_CARD) {
            return mCardStaggerList;
        } else if (mCardOnlyType == CardData.STAGGER_AND_ALL_CARD) {
            if (CardData.Category.CardBean.ALL_CARD == mCheckedType) {
                return mCardAllList;
            } else {
                return mCardStaggerList;
            }
        } else {
            return new ArrayList<>();
        }
    }

    public String getTimeText() {
        if (mCardResult == null) return "";
        List<CardData.Category.TimeLimitBean> timeLimitBeanList = new ArrayList<>();
        CardData.Category category = mCardResult.getCardData().getCategory();


        if (mCardOnlyType == CardData.ALL_CARD) {
            if (category.getAllDayCardBean() != null && category.getAllDayCardBean().getTimeLimitBeanList() != null) {
                timeLimitBeanList.addAll(category.getAllDayCardBean().getTimeLimitBeanList());
            }
        } else if (mCardOnlyType == CardData.STAGGER_CARD) {
            if (category.getFreeCardBean() != null && category.getFreeCardBean().getTimeLimitBeanList() != null) {
                timeLimitBeanList.addAll(timeLimitBeanList = category.getFreeCardBean().getTimeLimitBeanList());
            }
        } else if (mCardOnlyType == CardData.STAGGER_AND_ALL_CARD) {
            if (CardData.Category.CardBean.ALL_CARD == mCheckedType) {
                if (category.getAllDayCardBean() != null && category.getAllDayCardBean().getTimeLimitBeanList() != null) {
                    timeLimitBeanList.addAll(category.getAllDayCardBean().getTimeLimitBeanList());
                }
            } else {
                if (category.getFreeCardBean() != null && category.getFreeCardBean().getTimeLimitBeanList() != null) {
                    timeLimitBeanList.addAll(timeLimitBeanList = category.getFreeCardBean().getTimeLimitBeanList());
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (CardData.Category.TimeLimitBean bean : timeLimitBeanList) {
            sb.append(bean.getTitle()).append(": ").append(bean.getDesc());
            if (timeLimitBeanList.indexOf(bean) < timeLimitBeanList.size() - 1) {
                sb.append("\r\n").append("\r\n");
            }
        }
        return sb.toString();
    }

    public String getTitleText() {
        if (mCardResult == null) return "";
        String all = "全通卡";
        String stagger = "错峰卡";

        if (mCardOnlyType == CardData.ALL_CARD) {
            return all;
        } else if (mCardOnlyType == CardData.STAGGER_CARD) {
            return stagger;
        } else if (mCardOnlyType == CardData.STAGGER_AND_ALL_CARD) {
            if (CardData.Category.CardBean.ALL_CARD == mCheckedType) {
                return all;
            } else {
                return stagger;
            }
        } else {
            return "";
        }
    }

    /**
     * 健身卡列表
     *
     * @param gymId
     * @param type
     */
    public Observable<CardResult> getCardList(String gymId, int type) {

        String longitude = this.longitude;
        String latitude = this.latitude;

        if (longitude.equals("0.0") || latitude.equals("0.0")) {
            longitude = "0";
            latitude = "0";
        }

        Map<String, String> map = new HashMap<>();
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("city_id", cityId);
        map.put("district_id", districtId);
        map.put("gym_id", gymId);
        map.put("type", String.valueOf(type));
        return LikingNewApi.getInstance().getCardList(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<CardResult>applyHttpSchedulers())
                .doOnNext(new Consumer<CardResult>() {
                    @Override
                    public void accept(CardResult cardResult) throws Exception {
                        mCardResult = cardResult;
                        mGymData = cardResult.getCardData().getGymData();
                        mCardOnlyType = cardResult.getCardData().getType();
                        mCardAllList.clear();
                        mCardStaggerList.clear();
                        CardData.Category category = cardResult.getCardData().getCategory();
                        if (category.getAllDayCardBean() != null && category.getAllDayCardBean().getCardBeanList() != null) {
                            mCardAllList.addAll(category.getAllDayCardBean().getCardBeanList());
                        }
                        if (category.getFreeCardBean() != null && category.getFreeCardBean().getCardBeanList() != null) {
                            mCardStaggerList.addAll(category.getFreeCardBean().getCardBeanList());
                        }
                        isActivity = cardResult.getCardData().getGymActivityBean().getIs_activity() == CardData.GymActivityBean.HAVE_ACTIVITY;
                    }
                })
                ;
    }

    void getLocationData() {
        LocationData locationData = LikingPreference.getLocationData();
        if (locationData != null) {
            longitude = locationData.getLongitude() + "";
            latitude = locationData.getLatitude() + "";
            cityId = locationData.getCityId();
            districtId = locationData.getDistrictId();
        }
    }

    public String getGymId() {
        if (mGymData == null) return "";
        return mGymData.getGymId();
    }


}
