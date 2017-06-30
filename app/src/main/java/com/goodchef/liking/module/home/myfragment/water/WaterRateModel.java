package com.goodchef.liking.module.home.myfragment.water;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.WaterOrderResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:29
 * version 1.0.0
 */

public class WaterRateModel extends BaseModel {

    public List<WaterRateResult.DataBean.WaterListBean> mWaterRateResultList;

    public int mCheckedPos = -1;

    public int mPayWay = -1;

    public WaterRateModel() {
        mWaterRateResultList = new ArrayList<>();
    }

    public void onItemClick(int position) {
        if (mCheckedPos != -1)
            mWaterRateResultList.get(mCheckedPos).setChecked(false);
        mWaterRateResultList.get(position).setChecked(true);
        mCheckedPos = position;
    }

    public void savePayWay(int i) {
        mPayWay = i;
    }


    public void setWaterRateResultList(List<WaterRateResult.DataBean.WaterListBean> waterRateResultList) {
        this.mWaterRateResultList.clear();
        mCheckedPos = -1;
        if (mWaterRateResultList != null) {
            this.mWaterRateResultList = waterRateResultList;
            if (mWaterRateResultList.size() >= 1) {
                mWaterRateResultList.get(0).setChecked(true);
                mCheckedPos = 0;
            }
        }
    }

    /**
     * 获取水费列表
     *
     * @return Observable<WaterRateResult>
     */
    Observable<WaterRateResult> getWaterAllData() {
        return LikingNewApi.getInstance()
                .getWaterRateResult(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<WaterRateResult>applyHttpSchedulers())
                .doOnNext(new Consumer<WaterRateResult>() {
                    @Override
                    public void accept(WaterRateResult waterRateResult) throws Exception {
                        if (waterRateResult != null) {
                            setWaterRateResultList(waterRateResult.getData().getWater_list());
                        }
                    }
                })
                ;
    }

    /**
     * 获取 水费订单号
     * @return
     */
    Observable<WaterOrderResult> buyWaterRate() {
        return LikingNewApi.getInstance()
                .buyWaterRate(LikingNewApi.sHostVersion,
                        LikingPreference.getToken(),
                        LikingPreference.getLoginGymId(),
                        String.valueOf(mWaterRateResultList.get(mCheckedPos).getWater_id()),
                        String.valueOf(mPayWay))
                .compose(RxUtils.<WaterOrderResult>applyHttpSchedulers())
                ;
    }
}
