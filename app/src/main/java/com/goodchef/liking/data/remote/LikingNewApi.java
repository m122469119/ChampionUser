package com.goodchef.liking.data.remote;

import android.util.Log;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.http.retrofit.ServiceCreator;
import com.goodchef.liking.http.api.LikingCommonInterceptor;
import com.goodchef.liking.data.remote.converter.LikingGsonConvertFactory;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.SyncTimestampResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingNewApi {
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
