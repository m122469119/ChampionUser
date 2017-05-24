package com.goodchef.liking.module.home;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.remote.LikingNewApi;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.SyncTimestampResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:49
 * version 1.0.0
 */

public class LikingBaseModel extends BaseModel {

    /**
     * 同步时间戳
     *
     * @return SyncTimestampResult
     */
    Observable<SyncTimestampResult> syncServerTimestamp() {
        return LikingNewApi.getInstance().syncServerTimestamp();
    }


    /**
     * 基础配置
     *
     * @return BaseConfigResult
     */
    Observable<BaseConfigResult> baseConfig() {
        return LikingNewApi.getInstance().baseConfig();
    }

}
