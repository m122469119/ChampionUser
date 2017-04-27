package com.aaron.android.framework.library.http.volley;

import com.aaron.android.codelibrary.http.HttpRequestClient;
import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.library.http.RequestParams;
import com.aaron.android.framework.library.http.statistics.NetworkStatistics;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步网络请求的实现类，唯一的对外网络请求的接口
 * Created on 15/6/11.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class VolleyHttpRequestClient implements HttpRequestClient<VolleyRequestObjects> {

    private final static String TAG = "VolleyHttpRequestClient";
    public static NetworkStatistics sNetworkStatistics;

    /**
     * GET请求
     * @param url 请求地址
     * @param cls 解析数据类型
     * @param headers 请求头
     * @param params 请求参数
     * @param requestCallback 请求回调
     * @param <T> 数据类型 BaseResult泛型类型
     */
    public static <T extends BaseResult> void doGet(String url, Class<T> cls,
                                                  Map<String, String> headers,
                                                  Map<String, Object> params, RequestCallback<T> requestCallback) {
        VolleyHttpRequestClient volleyAsyncRequestClient = new VolleyHttpRequestClient();
        VolleyRequestObjects<T> objects = new VolleyRequestObjects<>(Request.Method.GET, url, cls, headers, params);
        volleyAsyncRequestClient.execute(requestCallback, objects);
    }

    /**
     * Post请求
     * @param url 请求地址
     * @param cls 请求数据类型
     * @param headers 请求头
     * @param requestParams 请求参数
     * @param requestCallback 请求回调
     * @param <T> 数据类型 BaseResult泛型类型
     */
    public static <T extends BaseResult> void doPost(String url, Class<T> cls,
                                  Map<String, String> headers,
                                  RequestParams requestParams, RequestCallback<T> requestCallback) {
        VolleyHttpRequestClient volleyAsyncRequest = new VolleyHttpRequestClient();
        Map<String, Object> params;
        if (requestParams != null) {
            params = requestParams.getParams();
        } else {
            params = new HashMap<>();
        }
        VolleyRequestObjects<T> objects = new VolleyRequestObjects<>(Request.Method.POST, url, cls, headers, params);
        volleyAsyncRequest.execute(requestCallback, objects);
    }
    /**
     * Post请求
     * @param url 请求地址
     * @param cls 请求数据类型
     * @param requestParams 请求参数
     * @param requestCallback 请求回调
     * @param <T> 数据类型 BaseResult泛型类型
     */
    public static <T extends BaseResult> void doPost(String url, Class<T> cls,
                                  RequestParams requestParams, RequestCallback<T> requestCallback) {
        doPost(url, cls, null, requestParams, requestCallback);
    }

    /**
     * 用Volley实现异步网络请求
     * @param requestObjects 请求参数集合
     * @param requestCallback 请求回调
     * @param <T> BaseResult泛型
     */
    @Override
    public <T extends BaseResult> void execute(RequestCallback<T> requestCallback, VolleyRequestObjects requestObjects) {
        VolleyResultRequest<T> request = requestObjects.buildVolleyResultRequest(requestCallback);
        if (requestCallback != null) {
            requestCallback.onStart();
        }
        VolleyRequestSingleton.getInstance().addToRequestQueue(request);
    }

    @Override
    public <T extends BaseResult> T execute(VolleyRequestObjects requestObjects) {
        return null;
    }
}
