package com.goodchef.liking.module.joinus.contractjoin;

import android.content.Context;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.ProgressObserver;
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
                PopupUtils.showToast(mContext.getString(R.string.name_not_blank));
                mView.setNameEditText();
                return;
            } else if (name.length() > 15) {
                PopupUtils.showToast(mContext.getString(R.string.name_length_surpass_15));
                mView.setNameEditText();
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                PopupUtils.showToast(mContext.getString(R.string.phone_not_blank));
                mView.setPhoneEditText();
                return;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                PopupUtils.showToast(mContext.getString(R.string.phone_input_error));
                mView.setPhoneEditText();
                return;
            }
            if (StringUtils.isEmpty(city)) {
                PopupUtils.showToast(mContext.getString(R.string.select_city));
                return;
            }
            mJoinModel.joinAppLy(UrlList.sHostVersion, name, phone, city, 0)
                .subscribe(new ProgressObserver<BaseResult>(mContext, R.string.loading) {
                    @Override
                    public void onNext(BaseResult result) {
                        super.onNext(result);
                        if (LiKingVerifyUtils.isValid(mContext, result)) {
                            mView.updateContactJoinContractView();
                        } else {
                            PopupUtils.showToast(result.getMessage());
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
