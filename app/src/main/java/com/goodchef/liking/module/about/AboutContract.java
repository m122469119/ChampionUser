package com.goodchef.liking.module.about;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;

/**
 * Created on 17/2/28.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public interface AboutContract {
    interface View extends BaseView {
        void updateVersionText(String version);

        void updateCooperatePhoneText(String cooperatePhone);

        void updateWeChatPublicAccountText(String weChatPublicAccount);
    }

    class Presenter extends RxBasePresenter<View> {

        public void init() {
            String version = ResourceUtils.getString(R.string.about_version) + EnvironmentUtils.Config.getAppVersionName();
            mView.updateVersionText(StringUtils.checkNotNull(version));
            String phone = LikingPreference.getBusinessServicePhone();
            if (!StringUtils.isEmpty(phone)) {
                mView.updateCooperatePhoneText(phone);
            }
            BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
            if (baseConfigResult != null) {
                BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                if (baseConfigData != null) {
                    mView.updateWeChatPublicAccountText(baseConfigData.getWechat().trim());
                }
            }
        }

    }
}
