package com.goodchef.liking.data.remote.retrofit;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.http.retrofit.ServiceCreator;
import com.goodchef.liking.data.remote.retrofit.converter.LikingGsonConvertFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingNewApi {
    public static String sHostVersion = "/v2";
    private static final int TIMEOUT = 10; //秒

    public static LikingApiService getInstance() {
        return LikingNewApiHolder.sLikingNewApiService;
    }

    private static class LikingNewApiHolder {
        static LikingApiService sLikingNewApiService = new ServiceCreator()
                .baseUrl(EnvironmentUtils.Config.getHttpRequestUrlHost())
                .connectTimeout(TIMEOUT)
                .readTimeout(TIMEOUT)
                .writeTimeout(TIMEOUT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(LikingGsonConvertFactory.create())
                .addInterceptor(new LikingCommonInterceptor())
                .build(LikingApiService.class);
    }
}
