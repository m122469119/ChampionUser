package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/3
 * @Version 1.0
 */
public class MilResult extends LikingResult {
    /**
     * directType : native
     * data : {"rec_user_id":["liking59a3ee6226060","liking599547d1b1fb4","liking598935d15f6a9","liking5981a3e74c1c2","liking598149fcc8e95"],"marathon_id":"1"}
     * direct : user_run
     * title : 好棒啊！挥洒汗水的马拉松小目标终于实现啦，看看你的成绩吧
     */

    @SerializedName("directType")
    private String directType;
    @SerializedName("data")
    private DataBean data;
    @SerializedName("direct")
    private String direct;
    @SerializedName("title")
    private String title;

    public String getDirectType() {
        return directType;
    }

    public void setDirectType(String directType) {
        this.directType = directType;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class DataBean extends Data {
        /**
         * rec_user_id : ["liking59a3ee6226060","liking599547d1b1fb4","liking598935d15f6a9","liking5981a3e74c1c2","liking598149fcc8e95"]
         * marathon_id : 1
         */

        @SerializedName("marathon_id")
        private String marathonId;
        @SerializedName("rec_user_id")
        private List<String> recUserId;

        @SerializedName("alert")
        private String alert;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getMarathonId() {
            return marathonId;
        }

        public void setMarathonId(String marathonId) {
            this.marathonId = marathonId;
        }

        public List<String> getRecUserId() {
            return recUserId;
        }

        public void setRecUserId(List<String> recUserId) {
            this.recUserId = recUserId;
        }
    }

}
