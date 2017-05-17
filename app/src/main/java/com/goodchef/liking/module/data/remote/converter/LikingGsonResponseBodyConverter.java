package com.goodchef.liking.module.data.remote.converter;

import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.module.data.remote.ApiException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created on 17/5/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson mGson;
    private TypeAdapter<T> mAdapter;

    LikingGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        mGson = gson;
        mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        LikingResult result = mGson.fromJson(response, LikingResult.class);
        if (!result.isSuccess()) {
            value.close();
            throw new ApiException(result.getCode(), result.getMessage());
        }

        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
        InputStreamReader reader = new InputStreamReader(bis, charset);
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try {
            return mAdapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
