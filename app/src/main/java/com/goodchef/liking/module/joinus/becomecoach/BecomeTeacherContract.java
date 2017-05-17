package com.goodchef.liking.module.joinus.becomecoach;

import android.content.Context;

import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.joinus.JoinModel;

/**
 * 说明:成为私教p层
 * Author : shaozucheng
 * Time: 下午3:43
 * version 1.0.0
 */

public interface BecomeTeacherContract {
    interface BecomeTeacherView extends BaseView {
        String getBecomeTeacherName();

        String getBecomeTeacherPhone();

        String getBecomeTeacherCity();

        void setBecomeTeacherNameEditText();

        void setBecomeTeacherPhoneEditText();

        void updateBecomeTeacherView();

        void showToast(String message);
    }

    class BecomeTeacherPresenter extends BasePresenter<BecomeTeacherView> {

        JoinModel mJoinModel;

        public BecomeTeacherPresenter(Context context, BecomeTeacherView mainView) {
            super(context, mainView);
            mJoinModel = new JoinModel();
        }

        public void sendConfirmRequest() {
            String name = mView.getBecomeTeacherName();
            String phone = mView.getBecomeTeacherPhone();
            String city = mView.getBecomeTeacherCity();

            if (StringUtils.isEmpty(name)) {
                mView.showToast(mContext.getString(R.string.name_not_blank));
                mView.setBecomeTeacherNameEditText();
                return;
            } else if (name.length() > 15) {
                mView.showToast(mContext.getString(R.string.name_length_surpass_15));
                mView.setBecomeTeacherNameEditText();
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                mView.showToast(mContext.getString(R.string.phone_not_blank));
                mView.setBecomeTeacherPhoneEditText();
                return;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                mView.showToast(mContext.getString(R.string.phone_input_error));
                mView.setBecomeTeacherPhoneEditText();
                return;
            }
            if (StringUtils.isEmpty(city)) {
                mView.showToast(mContext.getString(R.string.city_not_blank));
                return;
            }

            mJoinModel.joinAppLy(UrlList.sHostVersion, name, phone, city, 1)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                                mView.updateBecomeTeacherView();
                            } else {
                                mView.showToast(likingResult.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {

                        }
                    });
        }


    }
}
