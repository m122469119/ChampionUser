package com.goodchef.liking.module.train;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:55
 */

public class UserExerciseModel extends BaseModel {

    public Observable<UserExerciseResult> getExerciseData() {
        return LikingNewApi.getInstance().getUserExerciseData(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<UserExerciseResult>applyHttpSchedulers());
    }

}
