package com.goodchef.liking.module.writeuserinfo;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:13
 * version 1.0.0
 */

public class CompleteUserInfoModel extends BaseModel {

    /**
     * 上传图片
     *
     * @param img 图片
     * @return Observable<UserImageResult>
     */
    Observable<UserImageResult> uploadUserImage(String img) {
        if (EnvironmentUtils.Config.isTestMode()) {
            return LikingNewApi.getInstance().uploadDebugUserImage(img);
        } else {
            return LikingNewApi.getInstance().uploadUserImage(img);
        }
    }


    /**
     * 更新用户信息
     *
     * @param name     用户名称
     * @param avatar   头像地址
     * @param gender   性别
     * @param birthday 生日
     * @param weight   体重
     * @param height   身高
     * @return Observable<LikingResult>
     */
    Observable<LikingResult> updateUserInfo(String name, String avatar, Integer gender, String birthday, String weight, String height) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        if (!StringUtils.isEmpty(name)) {
            map.put("name", name);
        }
        if (!StringUtils.isEmpty(avatar)) {
            map.put("avatar", avatar);
        }
        if (gender != null) {
            map.put("gender", gender);
        }
        if (!StringUtils.isEmpty(birthday)) {
            map.put("birthday", birthday);
        }
        if (!StringUtils.isEmpty(weight)) {
            map.put("weight", weight);
        }
        if (!StringUtils.isEmpty(height)) {
            map.put("height", height);
        }
        return LikingNewApi.getInstance().updateUserInfo(UrlList.sHostVersion, map);
    }

    /**
     * 获取用户个人信息
     *
     * @return Observable<UserInfoResult>
     */
    Observable<UserInfoResult> getUserInfo() {
        return LikingNewApi.getInstance().getUserInfo(UrlList.sHostVersion, LikingPreference.getToken());
    }
}
