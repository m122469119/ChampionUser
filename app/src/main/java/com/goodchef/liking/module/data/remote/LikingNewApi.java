package com.goodchef.liking.module.data.remote;

import com.aaron.android.framework.library.http.retrofit.ServiceGenerator;
import com.goodchef.liking.http.api.LikingCommonInterceptor;

/**
 * Created on 17/3/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingNewApi {
    public static LikingApiService getInstance() {
        return LikingNewApiHolder.sLikingNewApiService;
    }

    private static class LikingNewApiHolder {
        static LikingApiService sLikingNewApiService = ServiceGenerator.createService(LikingApiService.class, new LikingCommonInterceptor());
    }
}
