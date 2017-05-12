package com.goodchef.liking.utils;

import android.content.Context;

import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/15 下午3:55
 */
public class UMengCountUtil {

    /**
     * U盟统计界面
     * @param context context
     * @param eventId 事件id
     * @param city 城市
     */
    public static void UmengCount(Context context, String eventId, String city) {
        HashMap<String, String> map = new HashMap<>();
        map.put("city", city);
        MobclickAgent.onEvent(context, eventId, map);
    }

    /**
     * U盟统计界面 不需要填写城市
     * @param context context
     * @param eventId 事件id
     */
    public static void UmengCount(Context context, String eventId) {
        String cityName ;
        LocationData locationData = LikingPreference.getLocationData();
        if (locationData !=null && !StringUtils.isEmpty(locationData.getCityName())){
            cityName = locationData.getCityName();
        }else {
            cityName ="定位失败";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("city", cityName);
        MobclickAgent.onEvent(context, eventId, map);
    }


    /**
     * U盟统计按钮的点击量
     * @param context context
     * @param eventId 事件id
     */
    public static  void  UmengBtnCount(Context context ,String eventId){
        String cityName ;
        LocationData locationData = LikingPreference.getLocationData();
        if (locationData !=null && !StringUtils.isEmpty(locationData.getCityName())){
            cityName = locationData.getCityName();
        }else {
            cityName ="定位失败";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("city", cityName);
        MobclickAgent.onEvent(context, eventId,map);
    }

    /**
     * U盟统计按钮的点击量
     * @param context context
     * @param eventId 事件id
     * @param city 城市
     */
    public static  void  UmengBtnCount(Context context ,String eventId,String city){
        HashMap<String, String> map = new HashMap<>();
        map.put("city", city);
        MobclickAgent.onEvent(context, eventId,map);
    }

}
