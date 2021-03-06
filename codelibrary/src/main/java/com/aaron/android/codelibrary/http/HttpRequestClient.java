package com.aaron.android.codelibrary.http;

import com.aaron.android.codelibrary.http.result.BaseResult;

import java.util.Map;
import java.util.Objects;

/**
 * 网络请求接口Interface,由具体的实现
 * Created on 15/6/13.
 *
 * @author HuangRan
 */
public interface HttpRequestClient<R extends RequestObjects> {
    /**
     * 异步请求
     * @param requestCallback 请求回调
     */
     <T extends BaseResult> void execute(RequestCallback<T> requestCallback, R requestObjects);

    /**
     * 同步请求
     * @param requestObjects 请求参数内容
     * @return 网络数据
     */
    <T extends BaseResult> T execute(R requestObjects);

}
