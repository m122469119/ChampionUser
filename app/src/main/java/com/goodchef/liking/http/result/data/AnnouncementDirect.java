package com.goodchef.liking.http.result.data;

import com.aaron.http.code.result.Data;

/**
 * Created on 2017/3/1
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class AnnouncementDirect extends Data {

    /**
     * directType : native
     * direct : announcement
     * data : {"aid":82,"gym_id":1,"gymName":"LikingFit复兴店"}
     */

    private String directType;
    private String direct;
    private NoticeData data;

    public String getDirectType() {
        return directType;
    }

    public void setDirectType(String directType) {
        this.directType = directType;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public NoticeData getData() {
        return data;
    }

    public void setData(NoticeData data) {
        this.data = data;
    }

}
