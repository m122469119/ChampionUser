package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.CheckGymListResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/12 下午2:19
 */
public interface CheckGymView extends BaseView {
    void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData);
}
