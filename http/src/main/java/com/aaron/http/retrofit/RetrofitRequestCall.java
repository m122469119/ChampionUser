package com.aaron.http.retrofit;


import com.aaron.http.code.RequestObjects;
import com.aaron.http.code.result.Result;

import retrofit2.Call;

/**
 * Created on 16/5/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class RetrofitRequestCall<T extends Result> extends RequestObjects {
    private Call<T> mCall;

    public RetrofitRequestCall(Call<T> call) {
        mCall = call;
    }

    public Call<T> getCall() {
        return mCall;
    }
}
