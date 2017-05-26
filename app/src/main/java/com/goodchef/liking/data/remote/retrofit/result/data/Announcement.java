package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;

import java.util.Set;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/5 下午3:06
 */
public class Announcement extends Data {
    private Set<String> mList;

    public Set<String> getList() {
        return mList;
    }

    public void setList(Set<String> list) {
        mList = list;
    }
}
