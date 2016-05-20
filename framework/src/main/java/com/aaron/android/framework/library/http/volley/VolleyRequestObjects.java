package com.aaron.android.framework.library.http.volley;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestObjects;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;

import java.util.Map;

/**
 * Created on 16/5/17.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class VolleyRequestObjects<T extends BaseResult> extends RequestObjects {
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
