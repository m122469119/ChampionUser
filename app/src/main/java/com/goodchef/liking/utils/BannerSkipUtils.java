package com.goodchef.liking.utils;

import android.content.Context;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.widget.web.HDefaultWebActivity;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.umeng.UmengEventId;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;


/**
 * Created on 16/2/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class BannerSkipUtils {

    public static final String SKIP_TYPE_H5 = "1";
    public static final String SKIP_TYPE_NATIVE = "0";
    public static final String SKIP_TYPE_NOTING = "2";

    public static void skip(Context context, BannerResult.BannerData.Banner banner) {
        if (banner == null) {
            return;
        }
        String type = banner.getType();
        if (StringUtils.isEmpty(type)) {
            return;
        }
        switch (type) {
            case SKIP_TYPE_H5: //跳转H5
                LocationData locationData = LikingPreference.getLocationData();
                if (locationData != null && !StringUtils.isEmpty(locationData.getCityName())) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("banner", locationData.getCityName() + "_" + banner.getLoadUrl());
                    MobclickAgent.onEvent(context, UmengEventId.BANNER, map);
                }
                HDefaultWebActivity.launch(context, banner.getLoadUrl(), banner.getTitle());
                break;
            case SKIP_TYPE_NATIVE: //跳转Native界面
                break;
            case SKIP_TYPE_NOTING: //只显示不跳转
                break;
            default:
                break;
        }
    }
}
