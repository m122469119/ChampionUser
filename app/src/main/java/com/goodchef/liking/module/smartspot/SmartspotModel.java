package com.goodchef.liking.module.smartspot;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;

import io.reactivex.Observable;

public class SmartspotModel extends BaseModel {

    public Observable<SmartspotDetailResult> getSmartspotHistory(String recordID) {
        return LikingNewApi.getInstance()
                .getSmartspotDetail(LikingNewApi.sHostVersion,
                        LikingPreference.getToken(),
                        recordID)
                .compose(RxUtils.<SmartspotDetailResult>applyHttpSchedulers());
    }
}
