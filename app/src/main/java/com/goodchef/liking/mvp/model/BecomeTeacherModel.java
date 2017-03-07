package com.goodchef.liking.mvp.model;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.goodchef.liking.http.api.LiKingApi;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:21
 * version 1.0.0
 */

public class BecomeTeacherModel  {
    public void joinAppLy(String name, String phone, String city, int type, RequestCallback callback) {
        LiKingApi.joinApply(name, phone, city, type, callback);
    }

}
