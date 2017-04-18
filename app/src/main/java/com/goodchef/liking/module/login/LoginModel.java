package com.goodchef.liking.module.login;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.storage.Preference;

import io.reactivex.Observable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class LoginModel {

    /**
     * 获取登录数据，存有用户相关信息
     * @param phone 手机号码
     * @param captcha 验证码
     * @return Observable<UserLoginResult>
     */
    Observable<UserLoginResult> getLoginResult(String phone, String captcha) {
        return LikingNewApi.getInstance().userLogin(UrlList.sHostVersion, phone, captcha);
    }

    /**
     * 登陆完成后保存相关用户信息
     * @param userLoginData 用户登陆
     */
    void saveLoginUserInfo(UserLoginResult.UserLoginData userLoginData) {
        Preference.setToken(userLoginData.getToken());
        Preference.setNickName(userLoginData.getName());
        Preference.setUserIconUrl(userLoginData.getAvatar());
        Preference.setUserPhone(userLoginData.getPhone());
        Preference.setIsNewUser(userLoginData.getNewUser());
        Preference.setIsVip(userLoginData.getIsVip());
    }
}
