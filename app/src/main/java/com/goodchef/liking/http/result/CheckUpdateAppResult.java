package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:检测更新APP
 * Author : shaozucheng
 * Time: 下午3:58
 * version 1.0.0
 */

public class CheckUpdateAppResult extends LikingResult {


    /**
     * data : {"update":1,"title":"更新提示","content":"app有新功能了，请更新体验","lastest_ver":"1.4.1","url":"http://testapp.likingfit.com/web/android/1.4.1.apk"}
     */

    @SerializedName("data")
    private UpdateAppData data;

    public UpdateAppData getData() {
        return data;
    }

    public void setData(UpdateAppData data) {
        this.data = data;
    }

    public static class UpdateAppData extends Data {
        /**
         * update : 1
         * title : 更新提示
         * content : app有新功能了，请更新体验
         * lastest_ver : 1.4.1
         * url : http://testapp.likingfit.com/web/android/1.4.1.apk
         */

        @SerializedName("update")
        private int update;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("lastest_ver")
        private String lastestVer;
        @SerializedName("url")
        private String url;

        public int getUpdate() {
            return update;
        }

        public void setUpdate(int update) {
            this.update = update;
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

        public String getLastestVer() {
            return lastestVer;
        }

        public void setLastestVer(String lastestVer) {
            this.lastestVer = lastestVer;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
