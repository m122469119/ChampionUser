package com.goodchef.liking.module.more;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.ConstantUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created on 17/3/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class MoreModel extends BaseModel {
    public static final int NO_UPDATE = 0;

    final Observable<CheckUpdateAppResult> getCheckUpdateAppResult() {
        return LikingNewApi.getInstance().getCheckUpdateAppResult(LikingNewApi.sHostVersion)
                .compose(RxUtils.<CheckUpdateAppResult>applyHttpSchedulers());
    }

    final Observable<LikingResult> userLogout(String version, String token, String registerId) {
        return LikingNewApi.getInstance().userLogout(version, token, registerId)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers())
                .doOnNext(new Consumer<LikingResult>() {
                    @Override
                    public void accept(LikingResult likingResult) throws Exception {
                        clearUserInfo();
                    }
                });
    }

    final void clearUserInfo() {
        LikingPreference.setToken(ConstantUtils.BLANK_STRING);
        LikingPreference.setNickName(ConstantUtils.BLANK_STRING);
        LikingPreference.setUserPhone(ConstantUtils.BLANK_STRING);
        LikingPreference.setIsNewUser(null);
        LikingPreference.setUserIconUrl(ConstantUtils.BLANK_STRING);
        LikingPreference.setIsBind("0");
        LikingPreference.clearHomeAnnouncement();
        LikingPreference.clearAnnouncementId();
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
