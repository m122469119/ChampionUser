package com.goodchef.liking.module.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aaron.common.utils.LogUtils;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:21
 * version 1.0.0
 */

public class NavigationMap {

    public static final String NAVIGATION_MAP_PACKAGENAME_BAIDU = "com.baidu.BaiduMap";

    public static final String NAVIGATION_MAP_PACKAGENAME_GAODE = "com.autonavi.minimap";

    public static final String NAVIGATION_MAP_PACKAGENAME_TENCENT = "com.tencent.map";

    public static Boolean isPackageInstalled(Context context, String packagename) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null != packageinfo;
    }

    public static void navigationGoMap(Context context, MapType mapType, Double latitude, Double longitude) {
        switch (mapType) {
            case BAIDU:
                navigationStart(context, "baidumap://map/geocoder?location=" + latitude + "," + longitude);
                break;
            case GAODE:
                navigationStart(context, "androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + latitude + "&dlon=" + longitude + "&dname=目的地&dev=0&t=2");
                break;
            case TENCENT:
                navigationStart(context, "qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + latitude + "()," + longitude + "&policy=0&referer=appName");
                break;
        }
    }

    private static void navigationStart(Context context, String scheme) {
        LogUtils.e("NavigationMapUtils", scheme);
        context.startActivity(new Intent("android.intent.action.VIEW", android.net.Uri.parse(scheme)));
    }

    enum MapType {
        BAIDU, GAODE, TENCENT
    }
}
