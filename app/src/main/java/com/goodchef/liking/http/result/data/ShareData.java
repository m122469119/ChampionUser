package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 上午10:06
 */
public class ShareData extends Data {
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
