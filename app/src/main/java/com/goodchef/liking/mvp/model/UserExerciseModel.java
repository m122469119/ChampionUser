package com.goodchef.liking.mvp.model;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:55
 */

public class UserExerciseModel {

    public void getExerciseData(RequestCallback requestCallback) {
        LiKingApi.getUserExerciseData(Preference.getToken(), requestCallback);
    }

}
