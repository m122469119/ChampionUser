package com.aaron.http.code;


import java.util.Map;

/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class RequestError {
    private ErrorType mErrorType;
    private NetworkErrorResponse mErrorNetworkErrorResponse;
    private String mMessage;
    private long networkTimeMs;
    private String mUrl;
    private Map<String, Object> mRequestParams;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public ErrorType getErrorType() {
        return mErrorType;
    }

    public void setErrorType(ErrorType errorType) {
        mErrorType = errorType;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Map<String, Object> getRequestParams() {
        return mRequestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        mRequestParams = requestParams;
    }

    public NetworkErrorResponse getErrorNetworkErrorResponse() {
        return mErrorNetworkErrorResponse;
    }

    public void setErrorNetworkErrorResponse(NetworkErrorResponse errorNetworkErrorResponse) {
        mErrorNetworkErrorResponse = errorNetworkErrorResponse;
    }

    public enum ErrorType {
        UNKNOWN, /*未知错误*/
        PARSE_ERROR, /*数据解析失败*/
        SERVER_ERROR, /*请求异常,有错误的Http状态码返回*/
        NETWORK_ERROR, /*网络异常（如连接失败，连接超时等）*/
        HTTP_ERROR, /*HTTP协议出错*/
    }

    public long getNetworkTimeMs() {
        return networkTimeMs;
    }

    public void setNetworkTimeMs(long networkTimeMs) {
        this.networkTimeMs = networkTimeMs;
    }
}
