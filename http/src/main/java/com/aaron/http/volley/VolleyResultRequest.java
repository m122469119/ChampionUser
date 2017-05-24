package com.aaron.http.volley;

import com.aaron.common.utils.LogUtils;
import com.aaron.http.code.NetworkErrorResponse;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.http.code.result.Result;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 请求BaseResult数据Request
 * 调整请求相关参数、处理BaseResult的解析(gson)以及对请求成功或失败的回调处理方式
 *
 * Created on 15/6/8.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class VolleyResultRequest<T extends Result> extends Request<T> {

    private static final String TAG = "ResultRequest";
    private Gson mGson = new Gson();
    private Map<String, Object> mParams;
    private Map<String, String> mHeaders;
    private Class<T> mClass;
    private static final int TIMEOUT_MS = 5000; //超时时间
    private static final int MAX_RETRIES_COUNT = 0; //最大重连次数
    private static final float BACKOFF_MULT = 1f;
    private RequestCallback<T> mRequestCallback;

    /**
     * Constructor
     *
     * @param method        method
     * @param url           链接
     * @param cls           class
     * @param headers       请求头
     * @param params        请求参数
     */
    public VolleyResultRequest(int method, String url, Class<T> cls,
                               Map<String, String> headers,
                               Map<String, Object> params, RequestCallback<T> requestCallback) {
        super(method, url, null);
        init(cls, headers, params, requestCallback);
    }

    /**
     * 无Headers Constructor
     * Constructor
     *
     * @param method        method
     * @param url           链接
     * @param cls           class
     * @param params        请求参数
     */
    public VolleyResultRequest(int method, String url, Class<T> cls, Map<String, Object> params, RequestCallback<T> requestCallback) {
        this(method, url, cls, null, params, requestCallback);
    }

    public void init(Class<T> cls,
                     Map<String, String> headers,
                     Map<String, Object> params,
                     RequestCallback<T> requestCallback) {
        mParams = params;
        mHeaders = headers;
        mClass = cls;
        mRequestCallback = requestCallback;
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES_COUNT, BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders == null ? super.getHeaders() : mHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (mParams != null && mParams.size() > 0) {
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = mParams.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, mParams.get(key).toString());
            }
            return map;
        }
        return super.getParams();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mRequestCallback != null) {
            mRequestCallback.onSuccess(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mRequestCallback != null) {
            RequestError requestError = null;
            RequestError.ErrorType errorType = null;
            if (error != null) {
                if (error instanceof ParseError) {
                    errorType = RequestError.ErrorType.PARSE_ERROR;
                } else if (error instanceof TimeoutError) {
//                    errorType = RequestError.ErrorType.TIMEOUT;
                } else if (error instanceof NoConnectionError) {
//                    errorType = RequestError.ErrorType.NO_CONNECTION;
                } else if (error instanceof ServerError) {
                    errorType = RequestError.ErrorType.SERVER_ERROR;
                } else if (error instanceof AuthFailureError) {
//                    errorType = RequestError.ErrorType.AUTH_FAILURE_ERROR;
                } else if (error instanceof NetworkError) {
                    errorType = RequestError.ErrorType.NETWORK_ERROR;
                }
                requestError = new RequestError();
                requestError.setUrl(getUrl());
                requestError.setRequestParams(mParams);
                requestError.setMessage(error.getMessage());
                requestError.setNetworkTimeMs(error.getNetworkTimeMs());
                requestError.setErrorType(errorType);
                LogUtils.e(TAG, "request http error: " + errorType);
                if (error.networkResponse != null) {
                    requestError.setErrorNetworkErrorResponse(new NetworkErrorResponse(error.networkResponse.statusCode,
                            error.networkResponse.data, error.networkResponse.headers,
                            error.networkResponse.notModified, error.networkResponse.networkTimeMs));
                }
            }
            mRequestCallback.onFailure(requestError);
            //TODO 网络异常上报应该放到app module处理
//            if (EnvironmentUtils.Network.isNetWorkAvailable() && VolleyHttpRequestClient.sNetworkStatistics != null) {
//                VolleyHttpRequestClient.sNetworkStatistics.post(requestError);
//            }
        }
    }



}
