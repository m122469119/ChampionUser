package com.goodchef.liking.module.joinus;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.module.data.remote.RxUtils;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:21
 * version 1.0.0
 */

public class JoinModel extends BaseModel {
    public Observable<LikingResult> joinAppLy(String version, String name, String phone, String city, int type) {
        return LikingNewApi.getInstance().joinApply(version, name, phone, city, type)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

}
