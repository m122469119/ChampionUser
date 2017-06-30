package com.goodchef.liking.module.home.myfragment.water;

import android.app.Activity;
import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.pay.alipay.OnAliPayListener;
import com.aaron.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.WaterOrderResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.data.remote.rxobserver.StateViewLoadingObserver;
import com.goodchef.liking.module.pay.PayModel;
import com.goodchef.liking.wxapi.WXEntryActivity;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created on 2017/6/27
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface WaterRateContract {
    interface View extends BaseStateView {
        void setWaterAdapter(List<WaterRateResult.DataBean.WaterListBean> list);

        void updateInfoData(WaterRateResult.DataBean data);
    }



    class Presenter extends RxBasePresenter<WaterRateContract.View> {

        WaterRateModel mModel;
        PayModel mPayModel;

        public Presenter(Activity c) {
            mModel = new WaterRateModel();
            mPayModel = new PayModel(c, new OnAliPayListener() {
                @Override
                public void confirm() {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    mView.showToast("支付成功");
                }

                @Override
                public void onFailure(String errorMessage) {
                    mView.showToast("支付失败");
                }
            }, new WeixinPayListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String errorMessage) {
                    mView.showToast("支付失败");
                }
            });
        }

        public void setWaterAdapter() {
            mView.setWaterAdapter(mModel.mWaterRateResultList);
        }

        public void onItemClick(int position) {
            mModel.onItemClick(position);
            setWaterAdapter();
        }

        public void buyWaterRate(Context context) {
            Observable.just("")
                    .filter(new Predicate<String>() {
                        @Override
                        public boolean test(String s) throws Exception {
                            if (mModel.mPayWay == -1 || mModel.mCheckedPos == -1) {
                                mView.showToast("必须选择充值时长");
                                return false;
                            }
                            return true;
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<WaterOrderResult>>() {
                        @Override
                        public ObservableSource<WaterOrderResult> apply(String s) throws Exception {
                            return mModel.buyWaterRate();
                        }
                    })
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<WaterOrderResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(WaterOrderResult value) {
                            if (value == null) return;
                            mPayModel.handlePay(value.getData().getPay_type(),
                                    value.getData().getAli_pay_token(),
                                    WXPayEntryActivity.PAY_TYPE_BUY_WATER,
                                    String.valueOf(value.getData().getOrder_id()),
                                    value.getData().getWx_prepay_id());
                        }
                    }));


        }

        public void savePayWay(int payWay) {
            mModel.savePayWay(payWay);
        }

        public void getWaterAll() {
            mModel.getWaterAllData()
                 .subscribe(addObserverToCompositeDisposable(new StateViewLoadingObserver<WaterRateResult>(mView) {
                     @Override
                     public void onNext(WaterRateResult value) {
                         super.onNext(value);
                         if (value == null) return;
                         if (value.getData() != null) {
                             mView.updateInfoData(value.getData());
                         }
                     }
            }));

        }



    }
}
