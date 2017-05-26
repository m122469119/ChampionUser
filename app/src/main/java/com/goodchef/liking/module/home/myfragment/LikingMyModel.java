package com.goodchef.liking.module.home.myfragment;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:29
 * version 1.0.0
 */

public class LikingMyModel extends BaseModel {

    /**
     * 获取我的个人信息
     *
     * @return Observable<MyUserOtherInfoResult>
     */
    Observable<MyUserOtherInfoResult> getMyUserInfoData() {
        return LikingNewApi.getInstance().getMyUserInfoData(LikingNewApi.sHostVersion, LikingPreference.getToken());
    }
}
