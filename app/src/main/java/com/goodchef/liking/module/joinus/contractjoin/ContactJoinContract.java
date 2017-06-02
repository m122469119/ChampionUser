package com.goodchef.liking.module.joinus.contractjoin;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.joinus.JoinModel;

/**
 * 说明:联系加盟
 * Author : shaozucheng
 * Time: 下午6:05
 * version 1.0.0
 */

interface ContactJoinContract {

    interface View extends BaseView {
        String getUserName();

        String getUserPhone();

        String getUserSelectCity();

        void setNameEditText();

        void setPhoneEditText();

        void updateContactJoinContractView();

        void showToast(String message);
    }

    class Presenter extends RxBasePresenter<View> {
        JoinModel mJoinModel;

        public Presenter() {
            mJoinModel = new JoinModel();
        }

        void sendConfirmRequest(Context context) {
            String name = mView.getUserName();
            String phone = mView.getUserPhone();
            String city = mView.getUserSelectCity();

            if (StringUtils.isEmpty(name)) {
                mView.showToast(R.string.name_not_blank);
                mView.setNameEditText();
                return;
            } else if (name.length() > 15) {
                mView.showToast(R.string.name_length_surpass_15);
                mView.setNameEditText();
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                mView.showToast(R.string.phone_not_blank);
                mView.setPhoneEditText();
                return;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                mView.showToast(R.string.phone_input_error);
                mView.setPhoneEditText();
                return;
            }
            if (StringUtils.isEmpty(city)) {
                mView.showToast(R.string.select_city);
                return;
            }
            mJoinModel.joinAppLy(LikingNewApi.sHostVersion, name, phone, city, 0)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading, mView) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            mView.updateContactJoinContractView();
                        }

                    }));
        }

    }

}
