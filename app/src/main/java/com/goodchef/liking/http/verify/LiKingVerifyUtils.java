package com.goodchef.liking.http.verify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.thread.TaskScheduler;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.http.helper.VerifyResultUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.SyncTimestampResult;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.utils.CityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created on 16/1/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LiKingVerifyUtils {

    public static final String TAG = "ChefStoveVerifyResultUtils";
    public static final String NULL_STRING = "";

    /**
     * 验证Result有效性,处理相关服务器响应的BaseResult错误码
     *
     * @param context      上下文资源
     * @param likingResult 需要校验的BaseResult
     * @return 是否为正确有效的BaseResult
     */
    public static boolean isValid(final Context context, final BaseView view, LikingResult likingResult) {
        if (VerifyResultUtils.checkResultSuccess(context, likingResult)) {
            return true;
        }
//        if (context instanceof Activity && ((Activity)context).isFinishing()) {
//            return false;
//        }
        if (likingResult != null) {
            final int errorCode = likingResult.getCode();
            LogUtils.e(TAG, "request server error: " + errorCode);
            switch (errorCode) {
                case LiKingRequestCode.LOGIN_TOKEN_INVALID:
                    LikingPreference.setToken(NULL_STRING);
                    LikingPreference.setNickName(NULL_STRING);
                    LikingPreference.setUserPhone(NULL_STRING);
                    LikingPreference.setIsNewUser(null);
                    LikingPreference.setUserIconUrl(NULL_STRING);
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    EventBus.getDefault().post(new LoginOutFialureMessage());
                    break;
                case LiKingRequestCode.REQEUST_TIMEOUT:
                    initApi(context);
                    break;
                case LiKingRequestCode.ILLEGAL_REQUEST:
                case LiKingRequestCode.ILLEGAL_ARGUMENT:
                case LiKingRequestCode.ARGUMENT_HIATUS_EXCEPTION:
                case LiKingRequestCode.DB_CONNECTION_EXCEPTION:
                case LiKingRequestCode.REDIS_CONNECTION_EXCEPTION:
                case LiKingRequestCode.SERVER_EXCEPTION:
                    if (view != null && view instanceof BaseNetworkLoadView) {
                        ((BaseNetworkLoadView) view).handleNetworkFailure();
                    }
                    break;
                case LiKingRequestCode.BUY_COURSES_ERROR:
                    showBuyCoursesErrorDialog(context, likingResult.getMessage());
                    break;
                case LiKingRequestCode.INVALID_MOBOLE_NUMBER:
                case LiKingRequestCode.GET_VERIFICATION_CODE_FAILURE:
                case LiKingRequestCode.VERIFICATION_INVALID:
                case LiKingRequestCode.LOGIN_FAILURE:
                case LiKingRequestCode.VERIFICATION_INCORRECT:
                case LiKingRequestCode.LOGOUT_FAILURE:
                case LiKingRequestCode.ILLEGAL_VERIFICATION_CODE:
                    PopupUtils.showToast(context, likingResult.getMessage());
                    break;
                default:
                    break;

            }
        }
        return false;
    }

    public static boolean isValid(final Context context, LikingResult likingResult) {
        return isValid(context, null, likingResult);
    }

    private static boolean mSyncTimestampIsLoading = false; //同步时间戳接口正在加载中
    public static BaseConfigResult sBaseConfigResult = null;
    private static boolean mBaseConfigInitSuccess = false; //BaseConfig未与服务器同步成功

    public static void initApi(final Context context) {
        if (mSyncTimestampIsLoading) {
            return;
        }
        LiKingApi.syncServerTimestamp(new RequestCallback<SyncTimestampResult>() {
            @Override
            public void onStart() {
                super.onStart();
                mSyncTimestampIsLoading = true;
            }

            @Override
            public void onSuccess(SyncTimestampResult result) {
                if (isValid(context, result)) {
                    long currentSystemSeconds = DateUtils.currentDataSeconds();
                    LiKingApi.sTimestampOffset = Long.parseLong(result.getData().getTimestamp())
                            + (currentSystemSeconds - LiKingApi.sRequestSyncTimestamp) / 2
                            - currentSystemSeconds;
                    getBaseConfig(context);
                    EventBus.getDefault().post(new InitApiFinishedMessage(true));
                } else {
                    mSyncTimestampIsLoading = false;
                    sBaseConfigResult = getLocalBaseConfig(context);//Preference.getBaseConfig();
                }
            }

            @Override
            public void onFailure(RequestError error) {
                sBaseConfigResult = getLocalBaseConfig(context);//Preference.getBaseConfig();
                EventBus.getDefault().post(new InitApiFinishedMessage(false));
                mSyncTimestampIsLoading = false;
            }
        });
    }

    public static void getBaseConfig(final Context context) {
        if (mBaseConfigInitSuccess && sBaseConfigResult != null) {
            EventBus.getDefault().post(new InitApiFinishedMessage(true));
            return;
        }
        LiKingApi.baseConfig(new RequestCallback<BaseConfigResult>() {
            @Override
            public void onStart() {
                super.onStart();
                mBaseConfigInitSuccess = true;
            }

            @Override
            public void onSuccess(final BaseConfigResult result) {
                if (isValid(context, result)) {
                    sBaseConfigResult = result;
                    BaseConfigResult.ConfigData baseConfigData = sBaseConfigResult.getBaseConfigData();
                    if (baseConfigData != null) {
                        UrlList.sHostVersion = File.separator + baseConfigData.getApiVersion();
                    }
                    //解析已开通城市
                    loadOpenCitysInfo(context);
                    LikingPreference.setBaseConfig(result);
                    EventBus.getDefault().post(new InitApiFinishedMessage(true));
                } else {
                    sBaseConfigResult = getLocalBaseConfig(context);//Preference.getBaseConfig();
                }
                mBaseConfigInitSuccess = true;
                mSyncTimestampIsLoading = false;
            }

            @Override
            public void onFailure(RequestError error) {
                mSyncTimestampIsLoading = false;
                mBaseConfigInitSuccess = false;
                sBaseConfigResult = getLocalBaseConfig(context);//Preference.getBaseConfig();
                EventBus.getDefault().post(new InitApiFinishedMessage(false));
            }
        });
    }


    public static List<City.RegionsData.CitiesData> getCitiesDataList() {
        List<City.RegionsData.CitiesData> citiesDatas = new ArrayList<>();
        BaseConfigResult baseResult = LikingPreference.getBaseConfig();
        if (baseResult == null) {
            return citiesDatas;
        }
        BaseConfigResult.ConfigData baseConfig = baseResult.getBaseConfigData();
        if (baseConfig == null) {
            return citiesDatas;
        }
        List<CityData> cityList = baseConfig.getCityList();
        if (ListUtils.isEmpty(cityList)) {
            return citiesDatas;
        }
        for (int i = 0; i < cityList.size(); i++) {
            City.RegionsData.CitiesData cityBean = new City.RegionsData.CitiesData();
            cityBean.setCityName(cityList.get(i).getCityName());
            cityBean.setCityId(cityList.get(i).getCityId() + "");
            citiesDatas.add(cityBean);
        }
        return citiesDatas;
    }


    /**
     * 加载以开放城市信息
     */
    public static void loadOpenCitysInfo(final Context context) {
        loadOpenCitysInfo(context, null);
    }

    public static void loadOpenCitysInfo(final Context context, final List<String> openCities) {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
                List<CityData> cityList = new ArrayList<>();
                List<String> openCityCodes;
                BaseConfigResult.ConfigData baseConfigData = sBaseConfigResult.getBaseConfigData();
                if (baseConfigData == null) {
                    return;
                }
                if (openCities == null) {
                    openCityCodes = baseConfigData.getOpenCity();
                } else {
                    openCityCodes = openCities;
                }

                for (String cityCode : openCityCodes) {
                    CityData cityData = null;
                    City.RegionsData.CitiesData crc = citiesMap.get(cityCode);
                    try {
                        if (crc != null) {
                            //城市
                            cityData = new CityData();
                            cityData.setCityId(Integer.valueOf(crc.getCityId()));
                            cityData.setCityName(crc.getCityName());
                            List<CityData.DistrictData> districtAll = new ArrayList<>();
                            cityData.setDistrict(districtAll);

                            //地方
                            List<City.RegionsData.CitiesData.DistrictsData> districts = crc.getDistricts();
                            if (districts != null) {
                                for (City.RegionsData.CitiesData.DistrictsData district : districts) {
                                    CityData.DistrictData districtData = new CityData.DistrictData();
                                    districtData.setDistrictId(Integer.parseInt(district.getDistrictId()));
                                    districtData.setDistrictName(district.getDistrictName());
                                    districtAll.add(districtData);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }

                    if (cityData != null) {
                        cityList.add(cityData);
                    }
                }
                sBaseConfigResult.getBaseConfigData().setCityList(cityList);
                LikingPreference.setBaseConfig(sBaseConfigResult);
            }
        });
    }

    public static boolean checkLogin(Context context) {
        if (context == null) {
            return false;
        }
        if (!LikingPreference.isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }

    private static BaseConfigResult getLocalBaseConfig(Context context) {
        BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
//        if(baseConfigResult == null){
//            baseConfigResult = getBaseConfigFromAsset(context);
//        }
        return baseConfigResult;
    }

//    private static BaseConfigResult getBaseConfigFromAsset(Context context){
//        try
//        {
//            InputStream inputStream = context.getAssets().open("BaseConfig.json");
//            InputStreamReader inputReader = new InputStreamReader(inputStream);
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line = "";
//            String result = "";
//            while ((line = bufReader.readLine())!=null){
//                result += line;
//            }
//            return new Gson().fromJson(result,BaseConfigResult.class);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 购买私教课 团体课，预约团体课
     *
     * @param context
     * @param message
     */
    public static void showBuyCoursesErrorDialog(final Context context, String message) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.diaog_got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(context, LikingHomeActivity.class);
//                context.startActivity(intent);
//                EventBus.getDefault().post(new CoursesErrorMessage());
                dialog.dismiss();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
    }

}
