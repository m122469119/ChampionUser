package com.goodchef.liking.module.opendoor;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.UserAuthCodeResult;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LikingNewApi;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:25
 * version 1.0.0
 */

public class OpenDoorModel extends BaseModel {

    public Observable<UserAuthCodeResult> getOpenPwd(int inout) {
        return LikingNewApi.getInstance().getOpenPwd(UrlList.sHostVersion, LikingPreference.getToken(), inout);
    }
}
