package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.Data;

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
