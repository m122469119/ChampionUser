package com.goodchef.liking.http.verify;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.library.http.helper.VerifyResultUtils;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.activity.ChangeGymActivity;
import com.goodchef.liking.activity.GroupCoursesChargeConfirmActivity;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.activity.InviteFriendsActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyCardDetailsActivity;
import com.goodchef.liking.activity.MyChargeGroupCoursesDetailsActivity;
import com.goodchef.liking.activity.MyInfoActivity;
import com.goodchef.liking.activity.MyPrivateCoursesDetailsActivity;
import com.goodchef.liking.activity.MyTrainDataActivity;
import com.goodchef.liking.activity.OpenTheDoorActivity;
import com.goodchef.liking.activity.OrderPrivateCoursesConfirmActivity;
import com.goodchef.liking.activity.PrivateLessonDetailsActivity;
import com.goodchef.liking.activity.SelectCoursesListActivity;
import com.goodchef.liking.activity.SelfHelpGroupActivity;
import com.goodchef.liking.activity.UpgradeAndContinueCardActivity;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.CityListResult;
import com.goodchef.liking.http.result.SyncTimestampResult;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.mvp.view.BaseLoginView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.CityUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * @param context 上下文资源
     * @param result  需要校验的BaseResult
     * @return 是否为正确有效的BaseResult
     */
    public static boolean isValid(final Context context, final BaseView view, BaseResult result) {
        if (VerifyResultUtils.checkResultSuccess(context, result)) {
            return true;
        }
//        if (context instanceof Activity && ((Activity)context).isFinishing()) {
//            return false;
//        }
        if (result != null) {
            final int errorCode = result.getCode();
            LogUtils.e(TAG, "request server error: " + errorCode);
            switch (errorCode) {
                case LiKingRequestCode.LOGIN_TOKEN_INVALID:
                    if (view != null && view instanceof BaseLoginView) {
                        ((BaseLoginView) view).updateNoLoginView();
                    } else {
                        Preference.setToken(NULL_STRING);
                        Preference.setNickName(NULL_STRING);
                        Preference.setUserPhone(NULL_STRING);
                        Preference.setIsNewUser(null);
                        Preference.setUserIconUrl(NULL_STRING);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        if (context instanceof GroupLessonDetailsActivity || context instanceof GroupCoursesChargeConfirmActivity
                                || context instanceof BuyCardConfirmActivity || context instanceof PrivateLessonDetailsActivity
                                || context instanceof OrderPrivateCoursesConfirmActivity || context instanceof MyChargeGroupCoursesDetailsActivity
                                || context instanceof MyInfoActivity || context instanceof MyPrivateCoursesDetailsActivity || context instanceof MyTrainDataActivity
                                || context instanceof UpgradeAndContinueCardActivity || context instanceof MyCardDetailsActivity
                                || context instanceof MyCardActivity || context instanceof InviteFriendsActivity || context instanceof OpenTheDoorActivity
                                || context instanceof SelfHelpGroupActivity || context instanceof SelectCoursesListActivity
                                || context instanceof ChangeGymActivity) {
                            ((Activity) context).finish();
                        }
                        EventBus.getDefault().post(new LoginOutFialureMessage());
                    }
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
                    showBuyCoursesErrorDialog(context, result.getMessage());
                    break;
                case LiKingRequestCode.INVALID_MOBOLE_NUMBER:
                case LiKingRequestCode.GET_VERIFICATION_CODE_FAILURE:
                case LiKingRequestCode.VERIFICATION_INVALID:
                case LiKingRequestCode.LOGIN_FAILURE:
                case LiKingRequestCode.VERIFICATION_INCORRECT:
                case LiKingRequestCode.LOGOUT_FAILURE:
                case LiKingRequestCode.ILLEGAL_VERIFICATION_CODE:
                    PopupUtils.showToast(result.getMessage());
                    break;
                default:
                    break;

            }
        }
        return false;
    }

    public static boolean isValid(final Context context, BaseResult result) {
        return isValid(context, null, result);
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
                    BaseConfigResult.BaseConfigData baseConfigData = sBaseConfigResult.getBaseConfigData();
                    if (baseConfigData != null) {
                        UrlList.HOST_VERSION = File.separator + baseConfigData.getApiVersion();
                    }
                    //解析已开通城市
                    loadOpenCitysInfo(context);
                    Preference.setBaseConfig(result);
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

        BaseConfigResult.BaseConfigData baseConfig = LiKingVerifyUtils.sBaseConfigResult.getBaseConfigData();
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

    public static void loadOpenCitysInfo(final Context context, final List<String> openCities){
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
                List<CityData> cityList = new ArrayList<>();
                List<String> openCityCodes;
                if (openCities == null) {
                    openCityCodes = sBaseConfigResult.getBaseConfigData().getOpenCity();
                } else {
                    openCityCodes = openCities;
                }

                for (String cityCode: openCityCodes) {
                    CityData cityData = null;
                    City.RegionsData.CitiesData crc = citiesMap.get(cityCode);
                    try {
                        if(crc != null) {
                            //城市
                            cityData = new CityData();
                            cityData.setCityId(Integer.valueOf(crc.getCityId()));
                            cityData.setCityName(crc.getCityName());
                            List<CityData.DistrictData> districtAll = new ArrayList<>();
                            cityData.setDistrict(districtAll);

                            //地方
                            List<City.RegionsData.CitiesData.DistrictsData> districts = crc.getDistricts();
                            if(districts != null) {
                                for (City.RegionsData.CitiesData.DistrictsData district:districts) {
                                    CityData.DistrictData districtData = new CityData.DistrictData();
                                    districtData.setDistrictId(Integer.parseInt(district.getDistrictId()));
                                    districtData.setDistrictName(district.getDistrictName());
                                    districtAll.add(districtData);
                                }
                            }
                        }
                    }catch (Exception e) {}

                    if(cityData !=null) {
                        cityList.add(cityData);
                    }
                }
                sBaseConfigResult.getBaseConfigData().setCityList(cityList);
                Preference.setBaseConfig(sBaseConfigResult);
            }
        });
    }

    public static boolean checkLogin(Context context) {
        if (context == null) {
            return false;
        }
        if (!Preference.isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }

    private static BaseConfigResult getLocalBaseConfig(Context context) {
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
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
