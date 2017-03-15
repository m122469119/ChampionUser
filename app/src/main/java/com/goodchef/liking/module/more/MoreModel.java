package com.goodchef.liking.module.more;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.ConstantUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.storage.Preference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/3/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class MoreModel {
    final Observable<CheckUpdateAppResult> getCheckUpdateAppResult() {
        return LikingNewApi.getInstance().getCheckUpdateAppResult(UrlList.sHostVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    final Observable<BaseResult> userLogout(String version, String token, String registerId) {
        return LikingNewApi.getInstance().userLogout(version, token, registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    final void clearUserInfo() {
        Preference.setToken(ConstantUtils.BLANK_STRING);
        Preference.setNickName(ConstantUtils.BLANK_STRING);
        Preference.setUserPhone(ConstantUtils.BLANK_STRING);
        Preference.setIsNewUser(null);
        Preference.setUserIconUrl(ConstantUtils.BLANK_STRING);
        Preference.setIsBind("0");
    }
}
