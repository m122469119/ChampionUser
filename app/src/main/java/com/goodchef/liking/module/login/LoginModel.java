package com.goodchef.liking.module.login;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.module.data.remote.RxUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.local.LikingPreference;

import io.reactivex.Observable;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class LoginModel extends BaseModel {

    /**
     * 获取登录数据，存有用户相关信息
     * @param phone 手机号码
     * @param captcha 验证码
     * @return Observable<UserLoginResult>
     */
    Observable<UserLoginResult> getLoginResult(String phone, String captcha) {
        return LikingNewApi.getInstance().userLogin(UrlList.sHostVersion, phone, captcha)
                .compose(RxUtils.<UserLoginResult>applyHttpSchedulers());
    }

    /**
     * 登陆完成后保存相关用户信息
     * @param userLoginData 用户登陆
     */
    void saveLoginUserInfo(UserLoginResult.UserLoginData userLoginData) {
        LikingPreference.setToken(userLoginData.getToken());
        LikingPreference.setNickName(userLoginData.getName());
        LikingPreference.setUserIconUrl(userLoginData.getAvatar());
        LikingPreference.setUserPhone(userLoginData.getPhone());
        LikingPreference.setIsNewUser(userLoginData.getNewUser());
        LikingPreference.setIsVip(userLoginData.getIsVip());
    }
}
