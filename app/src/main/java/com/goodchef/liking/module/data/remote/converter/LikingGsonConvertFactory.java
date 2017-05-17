package com.goodchef.liking.module.data.remote.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created on 17/5/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingGsonConvertFactory extends Converter.Factory {
    private final Gson mGson;

    public static LikingGsonConvertFactory create() {
        return new LikingGsonConvertFactory();
    }

    public LikingGsonConvertFactory() {
        mGson = new Gson();
    }
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new LikingGsonResponseBodyConverter<>(mGson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new LikingGsonRequestConverter<>(mGson, adapter);
    }
}
