package com.aaron.android.framework.library.http.retrofit;

import com.aaron.android.codelibrary.http.HttpRequestClient;
import com.aaron.android.codelibrary.http.NetworkErrorResponse;
import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.common.utils.LogUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 16/3/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class RetrofitHttpRequestClient implements HttpRequestClient<RetrofitRequestCall> {

    private static final String TAG = "RetrofitHttpRequestClient";

    public static <T extends BaseResult> void call(RequestCallback<T> requestCallback, Call<T> call) {
        RetrofitRequestCall<T> retrofitRequestCall = new RetrofitRequestCall<>(call);
        RetrofitHttpRequestClient retrofitHttpRequestClient = new RetrofitHttpRequestClient();
        retrofitHttpRequestClient.execute(requestCallback, retrofitRequestCall);
    }

    public static <T extends BaseResult> T call(Call<T> call) {
        RetrofitRequestCall<T> retrofitRequestCall = new RetrofitRequestCall<>(call);
        RetrofitHttpRequestClient retrofitHttpRequestClient = new RetrofitHttpRequestClient();
        return retrofitHttpRequestClient.execute(retrofitRequestCall);
    }

    @Override
    public <T extends BaseResult> void execute(final RequestCallback<T> requestCallback, RetrofitRequestCall requestObjects) {
        Call<T> call = buildRequest(requestObjects);
        if (requestCallback != null) {
            requestCallback.onStart();
        }
        call.enqueue(new Callback<T>() {
            private Response<T> mResponse;
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    if (requestCallback != null) {
                        requestCallback.onSuccess(response.body());
                    }
                } else {
                    String errorMessage = "retrofit request error code: " + response.code()
                            + "\nerror message: " + response.message();
                    LogUtils.d(TAG, errorMessage);
                    mResponse = response;
                    onFailure(call, new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (requestCallback != null) {
                    RequestError requestError = new RequestError();
                    if (mResponse != null) {
                        NetworkErrorResponse networkErrorResponse = null;
                        try {
                            networkErrorResponse = new NetworkErrorResponse(mResponse.code(),
                                    mResponse.errorBody().bytes(), null, false, 0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        requestError.setErrorNetworkErrorResponse(networkErrorResponse);
                        requestError.setErrorType(RequestError.ErrorType.SERVER_ERROR);
                        requestError.setMessage(t.getMessage());
                    } else {
                        requestError.setErrorType(RequestError.ErrorType.NO_CONNECTION);
                    }
                    requestCallback.onFailure(requestError);
                }
            }
        });
    }

    @Override
    public <T extends BaseResult> T execute(RetrofitRequestCall requestObjects) {
        Call<T> call = buildRequest(requestObjects);
        Response<T> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.d(TAG, "method execute(RetrofitRequestObjects) request error");
        }
        if (response == null) {
            return null;
        }
        return response.body();
    }

    public <T extends BaseResult> Call<T> buildRequest(RetrofitRequestCall<T> requestObjects) {
        Call<T> call;
        if (requestObjects != null) {
            call = requestObjects.getCall();
        } else {
            throw new IllegalArgumentException("RetrofitRequestObjects params must be not null");
        }
        if (call == null) {
            throw new NullPointerException("Retrofit request call must be not null");
        }
        return call;
    }
}
