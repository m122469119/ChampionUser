package com.goodchef.liking.module.opendoor;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.UserAuthCodeResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:25
 * version 1.0.0
 */

public class OpenDoorModel extends BaseModel {

    public Observable<UserAuthCodeResult> getOpenPwd(int inout) {
        return LikingNewApi.getInstance().getOpenPwd(LikingNewApi.sHostVersion, LikingPreference.getToken(), inout)
                .compose(RxUtils.<UserAuthCodeResult>applyHttpSchedulers());
    }
}
