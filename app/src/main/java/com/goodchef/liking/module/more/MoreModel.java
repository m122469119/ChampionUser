package com.goodchef.liking.module.more;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.common.utils.ConstantUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.local.LikingPreference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/3/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class MoreModel extends BaseModel {
    public static final int NO_UPDATE = 0;

    final Observable<CheckUpdateAppResult> getCheckUpdateAppResult() {
        return LikingNewApi.getInstance().getCheckUpdateAppResult(UrlList.sHostVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    final Observable<LikingResult> userLogout(String version, String token, String registerId) {
        return LikingNewApi.getInstance().userLogout(version, token, registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    final void clearUserInfo() {
        LikingPreference.setToken(ConstantUtils.BLANK_STRING);
        LikingPreference.setNickName(ConstantUtils.BLANK_STRING);
        LikingPreference.setUserPhone(ConstantUtils.BLANK_STRING);
        LikingPreference.setIsNewUser(null);
        LikingPreference.setUserIconUrl(ConstantUtils.BLANK_STRING);
        LikingPreference.setIsBind("0");
    }

    public void saveUpdateInfo(CheckUpdateAppResult.UpdateAppData updateAppData) {
        if (updateAppData == null) {
            return;
        }
        int update = updateAppData.getUpdate();
        if (update == NO_UPDATE) {//无更新
            LikingPreference.setUpdateApp(0);
        } else if (update == 1 || update == 2) {//有更新
            LikingPreference.setUpdateApp(1);
            LikingPreference.setNewApkName(updateAppData.getLastestVer());
        }
    }
}
