package com.goodchef.liking.module.home;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.retrofit.result.UnreadMessageResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:58
 * version 1.0.0
 */

public class LikingHomeModel extends BaseModel {

    /**
     * 检测更新
     *
     * @return Observable<CheckUpdateAppResult>
     */
    Observable<CheckUpdateAppResult> getUpdateApp() {
        return LikingNewApi.getInstance().getUpdateApp(LikingNewApi.sHostVersion)
                .compose(RxUtils.<CheckUpdateAppResult>applyHttpSchedulers());
    }

    Observable<UnreadMessageResult> getHasMessage() {
        if (LikingPreference.isLogin()) {
            return LikingNewApi.getInstance().getHasReadMessage(LikingNewApi.sHostVersion, LikingPreference.getToken())
                    .compose(RxUtils.<UnreadMessageResult>applyHttpSchedulers());
        } else {
            return LikingNewApi.getInstance().getHasReadMessage(LikingNewApi.sHostVersion)
                    .compose(RxUtils.<UnreadMessageResult>applyHttpSchedulers());
        }
    }
}
