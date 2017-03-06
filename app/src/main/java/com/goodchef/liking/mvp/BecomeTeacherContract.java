package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.RegularUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.BecomeTeacherModel;

/**
 * 说明:成为私教p层
 * Author : shaozucheng
 * Time: 下午3:43
 * version 1.0.0
 */

public interface BecomeTeacherContract {
    interface BecomeTeacherView extends BaseView{
        String getBecomeTeacherName();
        String getBecomeTeacherPhone();
        String getBecomeTeacherCity();
        void setBecomeTeacherNameEditText();
        void setBecomeTeacherPhoneEditText();
        void updateContactJoinView();
    }

      class BecomeTeacherPresenter extends BasePresenter<BecomeTeacherView>{

          BecomeTeacherModel mBecomeTeacherModel;

          public BecomeTeacherPresenter(Context context, BecomeTeacherView mainView) {
              super(context, mainView);
              mBecomeTeacherModel = new BecomeTeacherModel();
          }

          public void sendConfirmRequest(){
              String name = mView.getBecomeTeacherName();
              String phone = mView.getBecomeTeacherPhone();
              String city = mView.getBecomeTeacherCity();

              if (StringUtils.isEmpty(name)) {
                  PopupUtils.showToast(mContext.getString(R.string.name_not_blank));
                  mView.setBecomeTeacherNameEditText();
                  return;
              } else if (name.length() > 15) {
                  PopupUtils.showToast(mContext.getString(R.string.name_length_surpass_15));
                  mView.setBecomeTeacherNameEditText();
                  return;
              }
              if (StringUtils.isEmpty(phone)) {
                  PopupUtils.showToast(mContext.getString(R.string.phone_not_blank));
                  mView.setBecomeTeacherPhoneEditText();
                  return;
              }
              if (!RegularUtils.isMobileExact(phone)) {
                  PopupUtils.showToast(mContext.getString(R.string.phone_input_error));
                  mView.setBecomeTeacherPhoneEditText();
                  return;
              }
              if (StringUtils.isEmpty(city)) {
                  PopupUtils.showToast(mContext.getString(R.string.city_not_blank));
                  return;
              }
              mBecomeTeacherModel.joinAppLy(name,phone,city,1,new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading) {
                  @Override
                  public void onSuccess(BaseResult result) {
                      super.onSuccess(result);
                      if (LiKingVerifyUtils.isValid(mContext, result)) {
                          mView.updateContactJoinView();
                      } else {
                          PopupUtils.showToast(result.getMessage());
                      }
                  }

                  @Override
                  public void onFailure(RequestError error) {
                      super.onFailure(error);
                  }
              });

          }




      }
}
