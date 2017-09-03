package com.goodchef.liking.module.scanqrcode;


import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.QRCodeResult;

import io.reactivex.Observable;

/**
 * Created by aaa on 17/9/2.
 */

public class ScanQrCodeModel extends BaseModel {

    public Observable<QRCodeResult> scanQrCode(String code) {
        return LikingNewApi.getInstance().authQRCode(LikingNewApi.sHostVersion, LikingPreference.getToken(), code)
                .compose(RxUtils.<QRCodeResult>applyHttpSchedulers());
    }
}
