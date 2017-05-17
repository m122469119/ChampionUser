package com.goodchef.liking.module.data.remote;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created on 17/5/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class HttpResponseFunction<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.transformResponseThrowable(throwable));
    }
}
