package com.goodchef.liking.data.remote;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/5/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class RxUtils {
    public static <T> ObservableTransformer<T, T> applyHttpSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(RxUtils.<T>applyInitSchedulers())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applyInitSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {
                return upstream.filter(new Predicate<T>() {
                    @Override
                    public boolean test(T t) throws Exception {
                        LikingBaseRequestHelper.initTimestamp();
                        LikingBaseRequestHelper.initBaseConfig();
                        return true;
                    }
                });
            }
        };
    }

}
