package com.goodchef.liking.module.bodytest;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.remote.RxUtils;

import io.reactivex.Observable;

/**
 * Created on 2017/5/23
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class BodyTestModel {

    public Observable<BodyTestResult> getBodyData(String bodyId) {
        return LikingNewApi
                .getInstance()
                .getBodyTestData(UrlList.sHostVersion,
                        LikingPreference.getToken(),
                        bodyId)
                .compose(RxUtils.<BodyTestResult>applyHttpSchedulers());
    }

}
