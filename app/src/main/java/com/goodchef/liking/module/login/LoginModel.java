package com.goodchef.liking.module.login;

import android.os.Build;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.UserLoginResult;
import com.goodchef.liking.data.remote.retrofit.result.VerificationCodeResult;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.utils.NumberConstantUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class LoginModel extends BaseModel {

    Observable<VerificationCodeResult> getVerificationCode(String phone) {
        return LikingNewApi.getInstance()
                .getVerificationCode(phone)
                .compose(RxUtils.<VerificationCodeResult>applyHttpSchedulers());
    }

    /**
     * 获取登录数据，存有用户相关信息
     *
     * @param phone   手机号码
     * @param captcha 验证码
     * @return Observable<UserLoginResult>
     */
    Observable<UserLoginResult> getLoginResult(String phone, String captcha) {
        return LikingNewApi.getInstance().userLogin(LikingNewApi.sHostVersion, phone, captcha)
                .compose(RxUtils.<UserLoginResult>applyHttpSchedulers())
                .doOnNext(new Consumer<UserLoginResult>() {
                    @Override
                    public void accept(UserLoginResult userLoginResult) throws Exception {
                        saveLoginUserInfo(userLoginResult.getUserLoginData());
                    }
                });
    }

    /***
     * 上传设备信息
     *
     * @param device_id       设备id
     * @param device_token    设备token
     * @param registration_id 极光推送id
     */
    Observable<LikingResult> uploadUserDevice(String device_id, String device_token, String registration_id) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("device_id", device_id);
        map.put("device_token", device_token);
        map.put("registration_id", registration_id);
        map.put("os_version", Build.VERSION.RELEASE);
        map.put("phone_type", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);
        return LikingNewApi.getInstance().uploadUserDevice(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /**
     * 登陆完成后保存相关用户信息
     *
     * @param userLoginData 用户登陆
     */
    void saveLoginUserInfo(UserLoginResult.UserLoginData userLoginData) {
        if (userLoginData == null) {
            return;
        }
        LikingPreference.setToken(userLoginData.getToken());
        LikingPreference.setNickName(userLoginData.getName());
        LikingPreference.setUserIconUrl(userLoginData.getAvatar());
        LikingPreference.setUserPhone(userLoginData.getPhone());
        LikingPreference.setIsNewUser(userLoginData.getNewUser());
        LikingPreference.setIsVip(userLoginData.getIsVip());
        LikingPreference.setLoginGymId(userLoginData.getGymId());
        LikingPreference.clearAnnouncementId();
        LikingPreference.clearHomeAnnouncement();
        if (!StringUtils.isEmpty(userLoginData.getGymId()) && !userLoginData.getGymId().equals(NumberConstantUtil.STR_ZERO)) {
            LikingHomeActivity.gymId = userLoginData.getGymId();
        }
    }

}
