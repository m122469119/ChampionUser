package com.goodchef.liking.storage;

import com.aaron.android.codelibrary.utils.ConstantUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.storage.AbsPreference;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.Announcement;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.result.data.PatchData;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public static final String PATCH_DATA = "patchData";
    public static final String NULL_STRING = "";
    public static final String APP_UPDATE = "appUpdate";
    public static final String ANNOUNCEMENT_ID = "announcement_id";//首页公告id
    public static final String IS_VIP = "is_vip";//是否
    public static final String IS_BIND = "is_bind";
    public static final String KEY_BRACELET_FIRST_PROMPT = "key_bracelet_first_prompt";
    public static final String SHOW_DEFAULT_GYM_DIALOG = "SHOW_DEFAULT_GYM_DIALOG";
    public static List<String> announcementList = new ArrayList<>();
    public static final String NEW_APK_NAME = "new_apk_name";
    public static final String UPDATE_APP = "update_app";
    public static final String LOGIN_GYM_ID = "login_gym_id";//登录后返回的gymId


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
     * 设置第一次提示用户在我的界面查看手环数据
     *
     * @param isfirst
     * @return
     */
    public static boolean setFirstBindBracelet(boolean isfirst) {
        return setObject(KEY_BRACELET_FIRST_PROMPT, isfirst);
    }

    /**
     * 获取第一次提示用户在我的界面查看手环数据
     *
     * @return
     */
    public static boolean getFirstBindBracelet() {
        return (boolean) getObject(KEY_BRACELET_FIRST_PROMPT, true);
    }

    /**
     * 设置用户是否是VIP
     *
     * @param isVip
     * @return
     */
    public static boolean setIsVip(Integer isVip) {
        return setObject(IS_VIP, isVip);
    }

    /**
     * 设置新版APKName
     *
     * @param apkName
     * @return
     */
    public static boolean setNewApkName(String apkName) {
        return setObject(NEW_APK_NAME, apkName);
    }

    /**
     * 获取新版apkName
     *
     * @return
     */
    public static String getNewAPkName() {
        return (String) getObject(NEW_APK_NAME, "");
    }

    /**
     * 设置更新
     *
     * @param update
     * @return
     */
    public static boolean setUpdateApp(int update) {
        return setObject(UPDATE_APP, update);
    }

    /**
     * 获取更新
     *
     * @return
     */
    public static int getUpdateApp() {
        return (int) getObject(UPDATE_APP, 0);
    }

    /**
     * 设置是否绑定
     *
     * @param isBind
     * @return
     */
    public static boolean setIsBind(String isBind) {
        return setObject(IS_BIND, isBind);
    }

    /**
     * 获取是否绑定
     *
     * @return
     */
    public static boolean isBind() {
        boolean isBind = false;
        String bind = (String) getObject(IS_BIND, "0");
        if (!StringUtils.isEmpty(bind)) {
            if ("0".equals(bind)) {
                isBind = false;
            } else if ("1".equals(bind)) {
                isBind = true;
            }
        }

        return isBind;
    }


    /**
     * 设置是否需要升级app
     *
     * @param isUpdate 升级标志
     * @return
     */
    public static boolean setIsUpdateApp(boolean isUpdate) {
        return setObject(APP_UPDATE, isUpdate);
    }

    /**
     * 获取是否需要升级app
     * 默认是true
     *
     * @return
     */
    public static boolean getIsUpdate() {
        boolean isUpdate;
        isUpdate = (boolean) getObject(APP_UPDATE, true);
        return isUpdate;
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

    /**
     * 获取用户是否是是VIP
     *
     * @return
     */
    public static boolean isVIP() {
        boolean isVip = false;
        int is_vip = (int) getObject(IS_VIP, 0);
        if (is_vip == 0) {
            isVip = false;
        } else if (is_vip == 1) {
            isVip = true;
        }
        return isVip;
    }

    /**
     * 设置登录的gymId
     *
     * @param gymId 场馆id
     * @return
     */
    public static boolean setLoginGymId(String gymId) {
        return setObject(LOGIN_GYM_ID, gymId);
    }

    /**
     * 获取登录的gymId
     *
     * @return
     */
    public static String getLoginGymId() {
        return (String) getObject(LOGIN_GYM_ID, "");
    }

    /**
     * 判断当前用户是否有卡
     * @return
     */
    public static boolean getUserHasCard() {
        String gym = getLoginGymId();
        if (!StringUtils.isEmpty(gym) && !"0".equals(gym)) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * 设置是否弹出默认场馆对话框
//     *
//     * @param show
//     * @return
//     */
//    public static boolean setShowDefaultGymDialg(boolean show) {
//        return setObject(SHOW_DEFAULT_GYM_DIALOG, show);
//    }
//
//    /**
//     * 获取是否弹出默认对话框
//     *
//     * @return
//     */
//    public static boolean getShowDefaultGymDialg() {
//        return (boolean) getObject(SHOW_DEFAULT_GYM_DIALOG, true);
//    }

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

    /**
     * 设置jPush ID
     *
     * @param registrationId
     * @return
     */
    public static boolean setJPushRegistrationId(String registrationId) {
        return setObject(REGISTRATION_ID, registrationId);
    }

    /**
     * 获取Jpush ID
     *
     * @return
     */
    public static String getJPushRegistrationId() {
        return (String) getObject(REGISTRATION_ID, NULL_STRING);
    }

    /**
     * 设置公告id
     */
    public static boolean setAnnouncementId(String announcementId) {
        if (!StringUtils.isEmpty(announcementId)) {
            Announcement announcementObject = new Announcement();
            if (announcementList != null && announcementList.size() > 0) {
                for (int i = 0; i < announcementList.size(); i++) {
                    if (!announcementList.get(i).equals(announcementId)) {
                        announcementList.add(announcementId);
                    }
                }
            } else {
                announcementList.add(announcementId);
            }
            announcementObject.setList(announcementList);
            String announcement = new Gson().toJson(announcementObject);
            return setObject(ANNOUNCEMENT_ID, announcement);
        } else {
            return false;
        }
    }

    /***
     * 获取公告id
     *
     * @return
     */
    public static boolean isIdenticalAnnouncement(String announcementId) {
        boolean isIdenticalAnnouncement = false;
        if (!StringUtils.isEmpty(announcementId)) {
            String announcementStr = (String) getObject(ANNOUNCEMENT_ID, NULL_STRING);
            Announcement announcement = new Gson().fromJson(announcementStr, Announcement.class);
            if (announcement != null) {
                List<String> announcementList = announcement.getList();
                boolean ishas = false;
                String announcementIdStr = "";
                if (announcementList != null && announcementList.size() > 0) {
                    for (int i = 0; i < announcementList.size(); i++) {
                        if (announcementList.get(i).equals(announcementId)) {
                            ishas = true;
                            announcementIdStr = announcementList.get(i);
                            break;
                        }
                    }
                    if (ishas) {//如果存在
                        if (announcementIdStr.equals(announcementId)) {
                            isIdenticalAnnouncement = false;
                            return isIdenticalAnnouncement;
                        }
                    } else {//如果不存在
                        isIdenticalAnnouncement = true;
                        return isIdenticalAnnouncement;
                    }
                } else {
                    isIdenticalAnnouncement = true;
                }
            } else {
                isIdenticalAnnouncement = true;
            }

        } else {
            isIdenticalAnnouncement = false;
        }

        return isIdenticalAnnouncement;
    }

    public static boolean isNewVersion() {
        return !EnvironmentUtils.Config.getAppVersionName().equals(getAppVersion());
    }

    public static void setAppVersion(String appVersion) {
        setObject(APP_VERSION, appVersion);
    }

    public static String getAppVersion() {
        return (String) getObject(APP_VERSION, "");
    }

    /**
     * 设置客服电话
     *
     * @param phone
     * @return
     */
    public static boolean setCustomerServicePhone(String phone) {
        return setObject(CUSTOMER_PHONE, phone);
    }

    /**
     * 获取客服电话
     *
     * @return
     */
    public static String getCustomerServicePhone() {
        return (String) getObject(CUSTOMER_PHONE, ConstantUtils.BLANK_STRING);
    }

    /**
     * 设置商务合作电话
     *
     * @param phone
     * @return
     */
    public static boolean setBusinessPhone(String phone) {
        return setObject(BUSINESS_PHONE, phone);
    }

    /**
     * 设置蓝牙名称
     *
     * @param key  key
     * @param name 蓝牙名称
     * @return
     */
    public static boolean setBlueToothName(String key, String name) {
        return setObject(key, name);
    }

    /**
     * 获取蓝牙名称
     *
     * @param key
     * @return
     */
    public static String getBlueToothName(String key) {
        return (String) getObject(key, ConstantUtils.BLANK_STRING);
    }


    /***
     * 获取商务合作电话
     *
     * @return
     */
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


    public static boolean savePatchData(PatchData data) {
        String patchDataString = new Gson().toJson(data);
        return setObject(PATCH_DATA, patchDataString);
    }

    public static PatchData getPatchData() {
        String patchDataString = (String) getObject(PATCH_DATA, NULL_STRING);
        return new Gson().fromJson(patchDataString, PatchData.class);
    }

}
