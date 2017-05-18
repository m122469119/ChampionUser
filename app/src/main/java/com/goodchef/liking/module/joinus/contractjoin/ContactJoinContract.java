package com.goodchef.liking.module.joinus.contractjoin;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.joinus.JoinModel;

/**
 * 说明:联系加盟
 * Author : shaozucheng
 * Time: 下午6:05
 * version 1.0.0
 */

public interface ContactJoinContract {

    interface ContactJoinContractView extends BaseView {
        String getUserName();

        String getUserPhone();

        String getUserSelectCity();

        void setNameEditText();

        void setPhoneEditText();

        void updateContactJoinContractView();

        void showToast(String message);
    }

    class ContactJoinContractPresenter extends BasePresenter<ContactJoinContractView> {
        JoinModel mJoinModel;

        public ContactJoinContractPresenter(Context context, ContactJoinContractView mainView) {
            super(context, mainView);
            mJoinModel = new JoinModel();
        }

        public void sendConfirmRequest() {
            String name = mView.getUserName();
            String phone = mView.getUserPhone();
            String city = mView.getUserSelectCity();

            if (StringUtils.isEmpty(name)) {
                mView.showToast(mContext.getString(R.string.name_not_blank));
                mView.setNameEditText();
                return;
            } else if (name.length() > 15) {
                mView.showToast(mContext.getString(R.string.name_length_surpass_15));
                mView.setNameEditText();
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                mView.showToast(mContext.getString(R.string.phone_not_blank));
                mView.setPhoneEditText();
                return;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                mView.showToast(mContext.getString(R.string.phone_input_error));
                mView.setPhoneEditText();
                return;
            }
            if (StringUtils.isEmpty(city)) {
                mView.showToast(mContext.getString(R.string.select_city));
                return;
            }
            mJoinModel.joinAppLy(UrlList.sHostVersion, name, phone, city, 0)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                                mView.updateContactJoinContractView();
                            } else {
                                mView.showToast(likingResult.getMessage());
                            }
                        }

                    });
        }

    }

}
