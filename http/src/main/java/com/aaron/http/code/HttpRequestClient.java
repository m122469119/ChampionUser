package com.aaron.http.code;

import com.aaron.http.code.result.Result;

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
     <T extends Result> void execute(RequestCallback<T> requestCallback, R requestObjects);

    /**
     * 同步请求
     * @param requestObjects 请求参数内容
     * @return 网络数据
     */
    <T extends Result> T execute(R requestObjects);

}
