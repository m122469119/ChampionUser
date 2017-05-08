package com.aaron.android.framework.library.http.retrofit;


import com.aaron.android.codelibrary.http.RequestObjects;
import com.aaron.android.codelibrary.http.result.Result;

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
