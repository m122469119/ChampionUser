package com.aaron.http.volley;

import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestObjects;
import com.aaron.http.code.result.Result;

import java.util.Map;

/**
 * Created on 16/5/17.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class VolleyRequestObjects<T extends Result> extends RequestObjects {
    private static final String TAG = "VolleyRequestObjects";
    private int mMethod;
    private String mUrl;
    private Class<T> mClass;
    private Map<String, String> mHeaders;
    private Map<String, Object> mParams;

    public VolleyRequestObjects(int method, String url, Class<T> aClass, Map<String, String> headers, Map<String, Object> params) {
        mMethod = method;
        this.mUrl = url;
        mClass = aClass;
        mHeaders = headers;
        mParams = params;
    }

    public VolleyResultRequest<T> buildVolleyResultRequest(RequestCallback<T> requestCallback) {
        if (StringUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("request url must not be empty");
        }
        VolleyResultRequest<T> request = new VolleyResultRequest<>(mMethod, mUrl, mClass, mHeaders, mParams, requestCallback);
        LogUtils.i(TAG, "url = " + request.getUrl() + (mParams != null ? ("|params = " + mParams.toString()):""));
        return request;
    }

}
