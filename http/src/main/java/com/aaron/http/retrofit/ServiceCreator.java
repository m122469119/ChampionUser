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

    public ServiceCreator addNetworkInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            mOkHttpClientBuilder.addNetworkInterceptor(interceptor);
        }
        return this;
    }

    public <S> S build(Class<S> serviceClass) {
        Retrofit retrofit = mRetrofitBuilder.client(mOkHttpClientBuilder.build()).build();
        return retrofit.create(serviceClass);
    }

}
