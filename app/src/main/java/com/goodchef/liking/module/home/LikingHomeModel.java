package com.goodchef.liking.module.home;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.LikingNewApi;

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
        return LikingNewApi.getInstance().getUpdateApp(UrlList.sHostVersion);
    }
}
