package com.goodchef.liking.module.about;

import android.content.Context;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.module.data.local.Preference;

/**
 * Created on 17/2/28.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public interface AboutContract {
    interface AboutView extends BaseView {
        void updateVersionText(String version);

        void updateCooperatePhoneText(String cooperatePhone);

        void updateWeChatPublicAccountText(String weChatPublicAccount);
    }

    class AboutPresenter extends BasePresenter<AboutView> {
        public AboutPresenter(Context context, AboutView mainView) {
            super(context, mainView);
        }

        @Override
        public void init() {
            String version = ResourceUtils.getString(R.string.about_version) + EnvironmentUtils.Config.getAppVersionName();
            mView.updateVersionText(StringUtils.checkNotNull(version));
            String phone = Preference.getBusinessServicePhone();
            if (!StringUtils.isEmpty(phone)) {
                mView.updateCooperatePhoneText(phone);
            }
            BaseConfigResult baseConfigResult = Preference.getBaseConfig();
            if (baseConfigResult != null) {
                BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                if (baseConfigData != null) {
                    mView.updateWeChatPublicAccountText(baseConfigData.getWechat().trim());
                }
            }
        }

    }
}
