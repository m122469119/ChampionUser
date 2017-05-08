package com.goodchef.liking.module.train;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:55
 */

public class UserExerciseModel {

    public Observable<UserExerciseResult> getExerciseData() {
        return LikingNewApi.getInstance().getUserExerciseData(UrlList.sHostVersion, Preference.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
