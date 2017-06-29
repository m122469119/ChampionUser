package com.goodchef.liking.module.home.myfragment.water;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:29
 * version 1.0.0
 */

public class WaterRateModel extends BaseModel {




    public List<WaterRateResult.WaterListBean> mWaterRateResultList;

    public int checkedPos = 0;

    public enum PayWay{
        ALIPAY, WECHAT
    }

    public PayWay mPayWay;


    public WaterRateModel() {
        mWaterRateResultList = new ArrayList<>();
        mWaterRateResultList.add(new WaterRateResult.WaterListBean());
        mWaterRateResultList.add(new WaterRateResult.WaterListBean());
        mWaterRateResultList.add(new WaterRateResult.WaterListBean());
        mWaterRateResultList.add(new WaterRateResult.WaterListBean());
        mWaterRateResultList.add(new WaterRateResult.WaterListBean());
    }

    public void onItemClick(int position) {
        mWaterRateResultList.get(checkedPos).setChecked(false);
        mWaterRateResultList.get(position).setChecked(true);
        checkedPos = position;
    }

    public void savePayWay(PayWay i) {
        mPayWay = i;
    }
}
