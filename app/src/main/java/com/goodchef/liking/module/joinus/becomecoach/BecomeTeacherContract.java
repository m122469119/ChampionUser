package com.goodchef.liking.module.joinus.becomecoach;

import android.content.Context;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.ProgressObserver;
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
                    .subscribe(new ProgressObserver<BaseResult>(mContext, R.string.loading) {
                        @Override
                        public void onNext(BaseResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateBecomeTeacherView();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }


    }
}
