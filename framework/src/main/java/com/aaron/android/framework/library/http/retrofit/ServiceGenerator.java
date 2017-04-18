package com.aaron.android.framework.library.http.retrofit;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 16/3/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class ServiceGenerator {
    private final static String BASE_URL = EnvironmentUtils.Config.getHttpRequestUrlHost();

    private static OkHttpClient.Builder sOkHttpClientBuilder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS);

    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    public static <S> S createService(Class<S> serviceClass, Interceptor interceptor) {
        Retrofit retrofit = sRetrofitBuilder.client(sOkHttpClientBuilder.addNetworkInterceptor(interceptor).build()).build();
        return retrofit.create(serviceClass);
    }
}
