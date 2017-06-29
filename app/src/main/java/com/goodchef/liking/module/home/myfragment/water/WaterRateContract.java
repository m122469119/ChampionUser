package com.goodchef.liking.module.home.myfragment.water;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;

import java.util.List;

/**
 * Created on 2017/6/27
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface WaterRateContract {
    interface View extends BaseStateView {
        void setWaterAdapter(List<WaterRateResult.WaterListBean> list);
    }



    class Presenter extends RxBasePresenter<WaterRateContract.View> {

        WaterRateModel mModel;

        public Presenter() {
            mModel = new WaterRateModel();
        }

        public void setWaterAdapter() {
            mView.setWaterAdapter(mModel.mWaterRateResultList);
        }

        public void onItemClick(int position) {
            mModel.onItemClick(position);
            setWaterAdapter();
        }

        public void buyWaterRate() {

        }

        public void savePayWay(WaterRateModel.PayWay payWay) {
            mModel.savePayWay(payWay);
        }
    }
}
