package com.goodchef.liking.module.message;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.MessageResult;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:20
 * version 1.0.0
 */

public class MessageModel extends BaseModel {

    Observable<MessageResult> getMessageList(int page) {
        return LikingNewApi.getInstance().getMessageList(LikingNewApi.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<MessageResult>applyHttpSchedulers());
    }

    Observable<LikingResult> setMessageRead(String messageId) {
        return LikingNewApi.getInstance().setMessageRead(LikingNewApi.sHostVersion, LikingPreference.getToken(), messageId)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }
}
