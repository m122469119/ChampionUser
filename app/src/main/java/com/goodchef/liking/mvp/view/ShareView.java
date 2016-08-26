package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.data.ShareData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 上午10:25
 */
public interface ShareView extends BaseView {
    void updateShareView(ShareData shareData);
}
