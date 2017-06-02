package com.goodchef.liking.module.joinus.becomecoach;

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
 * 说明:成为私教p层
 * Author : shaozucheng
 * Time: 下午3:43
 * version 1.0.0
 */

interface BecomeTeacherContract {
    interface View extends BaseView {
        String getBecomeTeacherName();

        String getBecomeTeacherPhone();

        String getBecomeTeacherCity();

        void setBecomeTeacherNameEditText();

        void setBecomeTeacherPhoneEditText();

        void updateBecomeTeacherView();

        void showToast(String message);
    }

    class Presenter extends RxBasePresenter<View> {

        JoinModel mJoinModel;

        public Presenter() {
            mJoinModel = new JoinModel();
        }

        void sendConfirmRequest(Context context) {
            String name = mView.getBecomeTeacherName();
            String phone = mView.getBecomeTeacherPhone();
            String city = mView.getBecomeTeacherCity();

            if (StringUtils.isEmpty(name)) {
                mView.showToast(R.string.name_not_blank);
                mView.setBecomeTeacherNameEditText();
                return;
            } else if (name.length() > 15) {
                mView.showToast(R.string.name_length_surpass_15);
                mView.setBecomeTeacherNameEditText();
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                mView.showToast(R.string.phone_not_blank);
                mView.setBecomeTeacherPhoneEditText();
                return;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                mView.showToast(R.string.phone_input_error);
                mView.setBecomeTeacherPhoneEditText();
                return;
            }
            if (StringUtils.isEmpty(city)) {
                mView.showToast(R.string.city_not_blank);
                return;
            }

            mJoinModel.joinAppLy(LikingNewApi.sHostVersion, name, phone, city, 1)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading, mView) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            mView.updateBecomeTeacherView();
                        }

                    }));
        }


    }
}
