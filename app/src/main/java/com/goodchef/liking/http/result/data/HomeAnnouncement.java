package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class HomeAnnouncement extends BaseData {
    Set<NoticeData> mNoticeDatas;


    public HomeAnnouncement() {
        mNoticeDatas = new HashSet<>();
    }

    public void addNotice(NoticeData noticeData){
        mNoticeDatas.add(noticeData);
    }

    public Set<NoticeData> getNoticeDatas() {
        return mNoticeDatas;
    }

    public void setNoticeDatas(Set<NoticeData> noticeDatas) {
        this.mNoticeDatas = noticeDatas;
    }
}
