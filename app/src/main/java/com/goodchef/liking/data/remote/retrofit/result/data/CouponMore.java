package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;

/**
 * Created on 2017/3/11
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponMore extends Data {
    String title;
    String content;

    public CouponMore() {
    }

    public CouponMore(String title, String content) {
        this.title = title;
        this.content = content;
    }

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
}
