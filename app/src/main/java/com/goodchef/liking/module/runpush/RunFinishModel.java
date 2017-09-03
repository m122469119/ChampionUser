package com.goodchef.liking.module.runpush;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.module.smartspot.SmartspotDetailResult;

import io.reactivex.Observable;

public class RunFinishModel extends BaseModel {

    public Observable<RunFinishResult> getRunFinsh(String userId, String marahtonId) {
        return LikingNewApi.getInstance()
                .getMarathon(LikingNewApi.sHostVersion,
                        LikingPreference.getToken(),
                        userId, marahtonId)
                .compose(RxUtils.<RunFinishResult>applyHttpSchedulers());
    }

    public Observable<FollowUserResult> followUser(String userId) {
        return LikingNewApi.getInstance()
                .followUser(LikingNewApi.sHostVersion,
                        LikingPreference.getToken(),
                        userId)
                .compose(RxUtils.<FollowUserResult>applyHttpSchedulers());
    }
}
