package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/5 下午3:06
 */
public class Announcement extends BaseData{
    private List<String> mList;

    public List<String> getList() {
        return mList;
    }

    public void setList(List<String> list) {
        mList = list;
    }
}
