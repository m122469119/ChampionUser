package com.goodchef.liking.storage;

import android.content.SharedPreferences;

import com.aaron.android.codelibrary.utils.ConstantUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.storage.AbsPreference;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.google.gson.Gson;

import java.io.File;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午11:49
 */
public class Preference extends AbsPreference {
    public static final String TOKEN = "token";
    public static final String NICKNAME = "nickname";
    public static final String USER_ICON_URL = "headimgurl";
    public static final String SHOW_PHONE = "showPhone";
    public static final String IS_NEW_USER = "isNewUser";
    public static final String APP_VERSION = "app_version";
    public static final String IS_GET_API_MESSAGE = "isGetApiFinishedMessage";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String CUSTOMER_PHONE = "customer_phone";//客服电话
    public static final String BUSINESS_PHONE = "business_phone";//商务合作电话
    public static final String BASE_CONFIG = "base_config";
    private static final String TAG = "Preference";
    public static final String NULL_STRING = "";


    /**
     * 清空SharedPreferences
     */
    public static void clear() {
        getInstance().edit().clear().commit();
    }

    /**
     * 根据Key移除SharedPreferences
     *
     * @param key key
     */
    public static void remove(String key) {
        getInstance().edit().remove(key).commit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    Key
     * @param object 需要存的引用对象
     * @return 数据是否成功存储
     */
    private static boolean setObject(String key, Object object) {
        if (object == null) {
            return false;
        }
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = getInstance().edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        return editor.commit(); //为保证后面取值的正确性,这里使用同步存储(线程阻塞)commit方法
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           Key
     * @param defaultObject 默认值
     * @return 取值
     */
    private static Object getObject(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return getInstance().getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return getInstance().getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return getInstance().getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return getInstance().getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return getInstance().getLong(key, (Long) defaultObject);
        }

        return null;
    }


    /**
     * 获取token
     *
     * @return token
     */
    public static String getToken() {
        String token = (String) getObject(TOKEN, "");
        return token;
    }

    /**
     * 保存token
     *
     * @param token token
     */
    public static boolean setToken(String token) {
        return setObject(TOKEN, token);
    }


    /**
     * 判断是否处于登录状态
     *
     * @return true or false
     */
    public static boolean isLogin() {
        String token = getToken();
        if (!StringUtils.isEmpty(token) && token.length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存用户昵称
     *
     * @param nickName 昵称
     */
    public static boolean setNickName(String nickName) {
        return setObject(NICKNAME, nickName);
    }

    public static String getNickName() {
        String name = (String) getObject(NICKNAME, NULL_STRING);
        return name;
    }

    /**
     * 获取用户头像url
     *
     * @return UserIconUrl
     */
    public static String getUserIconUrl() {
        String headUrl = (String) getObject(USER_ICON_URL, NULL_STRING);
        return headUrl;
    }

    /**
     * 保存用户头像url
     *
     * @param url 头像url
     */
    public static boolean setUserIconUrl(String url) {
        return setObject(USER_ICON_URL, url);
    }

    /**
     * 获取用户手机号码
     *
     * @return phone
     */
    public static String getUserPhone() {
        String phone = (String) getObject(SHOW_PHONE, NULL_STRING);
        return phone;
    }

    /**
     * 保存手机号码
     *
     * @param phone 手机号码
     */
    public static boolean setUserPhone(String phone) {
        return setObject(SHOW_PHONE, phone);
    }

    /***
     * 设置用户是否是新用户
     *
     * @param isNewUser 新用户标识
     * @return
     */
    public static boolean setIsNewUser(Integer isNewUser) {
        return setObject(IS_NEW_USER, isNewUser);
    }

    /**
     * 判断用户是否是新用户
     *
     * @return true or  false
     */
    public static boolean isNewUser() {
        boolean isNewUser = false;
        int userTag = (int) getObject(IS_NEW_USER, 0);
        if (userTag == 0) {
            isNewUser = false;
        } else if (userTag == 1) {
            isNewUser = true;
        }
        return isNewUser;
    }

    /***
     * 设置定位信息
     *
     * @param locationData
     * @return
     */
    public static boolean setLocationData(LocationData locationData) {
        if (locationData != null) {
            String locationString = new Gson().toJson(locationData);
            if (!setObject("locationData", locationString)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取定位信息
     *
     * @return
     */
    public static LocationData getLocationData() {
        String locationString = (String) getObject("locationData", NULL_STRING);
        return new Gson().fromJson(locationString, LocationData.class);
    }


    public static boolean setJPushRegistrationId(String registrationId) {
        return setObject(REGISTRATION_ID, registrationId);
    }

    public static String getJPushRegistrationId() {
        return (String) getObject(REGISTRATION_ID, NULL_STRING);
    }

    public static boolean isNewVersion() {
        return !EnvironmentUtils.Config.getAppVersionName().equals(getAppVersion());
    }

    public static void setAppVersion(String appVersion) {
        setObject(APP_VERSION,appVersion);
    }

    public static String getAppVersion() {
        return (String) getObject(APP_VERSION,"");
    }


    public static boolean setCustomerServicePhone(String phone) {
        return setObject(CUSTOMER_PHONE, phone);
    }

    public static String getCustomerServicePhone() {
        return (String) getObject(CUSTOMER_PHONE, ConstantUtils.BLANK_STRING);
    }

    public static boolean setBusinessPhone(String phone) {
        return setObject(BUSINESS_PHONE, phone);
    }

    public static String getBusinessServicePhone() {
        return (String) getObject(BUSINESS_PHONE, ConstantUtils.BLANK_STRING);
    }


    public static boolean setBaseConfig(BaseConfigResult baseConfigResult) {
        if (baseConfigResult != null && baseConfigResult.getBaseConfigData() != null) {
            if (!setCustomerServicePhone(baseConfigResult.getBaseConfigData().getCustomerPhone())) {
                return false;
            }
            if (!setBusinessPhone(baseConfigResult.getBaseConfigData().getBusinessPhone())) {
                return false;
            }
            String apiVersion = baseConfigResult.getBaseConfigData().getApiVersion();
            if (!StringUtils.isEmpty(apiVersion)) {
                UrlList.HOST_VERSION = File.separator + apiVersion;
            }
        }
        String baseConfig = new Gson().toJson(baseConfigResult);
        if (!setObject(BASE_CONFIG, baseConfig)) {
            return false;
        }
        LogUtils.i(TAG, "put baseConfig: " + baseConfig);
        return true;
    }

    public static BaseConfigResult getBaseConfig() {
        String baseConfig = (String) getObject(BASE_CONFIG, NULL_STRING);
        return new Gson().fromJson(baseConfig, BaseConfigResult.class);
    }

}
