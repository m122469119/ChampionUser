package com.aaron.http.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created on 16/3/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
//public class ServiceGenerator {
//    private final static String BASE_URL = EnvironmentUtils.Config.getHttpRequestUrlHost();
//    private static final int CONNECT_TIMEOUT = 10; //秒
//    private static OkHttpClient.Builder sOkHttpClientBuilder = new OkHttpClient
//            .Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
//    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder()
//            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//
//    /**
//     * @param serviceClass api class<>
//     * @param interceptor  添加公共参数
//     * @param <S>          api class
//     * @return
//     */
//    public static <S> S createService(Class<S> serviceClass, Interceptor interceptor) {
//        Retrofit retrofit = sRetrofitBuilder.client(
//                sOkHttpClientBuilder.addInterceptor(interceptor).build()).build();
//        return retrofit.create(serviceClass);
//    }

public class ServiceCreator {
    private OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
    private Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder();

    public ServiceCreator baseUrl(String baseUrl) {
        mRetrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    public ServiceCreator connectTimeout(int seconds) {
        mOkHttpClientBuilder.connectTimeout(seconds, TimeUnit.SECONDS);
        return this;
    }

    public ServiceCreator readTimeout(int seconds) {
        mOkHttpClientBuilder.readTimeout(seconds, TimeUnit.SECONDS);
        return this;
    }

    public ServiceCreator writeTimeout(int seconds) {
        mOkHttpClientBuilder.writeTimeout(seconds, TimeUnit.SECONDS);
        return this;
    }

    public ServiceCreator addConverterFactory(Converter.Factory factory) {
        if (factory != null) {
            mRetrofitBuilder.addConverterFactory(factory);
        }
        return this;
    }

    public ServiceCreator addCallAdapterFactory(CallAdapter.Factory factory) {
        if (factory != null) {
            mRetrofitBuilder.addCallAdapterFactory(factory);
        }
        return this;
    }

    public ServiceCreator addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            mOkHttpClientBuilder.addInterceptor(interceptor);
        }
        return this;
    }
    public <S> S build(Class<S> serviceClass) {
        Retrofit retrofit = mRetrofitBuilder.client(mOkHttpClientBuilder.build()).build();
        return retrofit.create(serviceClass);
    }
}
//}
