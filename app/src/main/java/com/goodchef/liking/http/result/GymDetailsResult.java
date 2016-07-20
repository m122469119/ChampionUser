package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 下午4:32
 */
public class GymDetailsResult extends BaseResult {
    @SerializedName("data")
    private GymDetailsData data;

    public GymDetailsData getData() {
        return data;
    }

    public void setData(GymDetailsData data) {
        this.data = data;
    }

    public static class GymDetailsData extends BaseData {
        @SerializedName("name")
        private String name;
        @SerializedName("announcement")
        private String announcement;
        @SerializedName("address")
        private String address;
        @SerializedName("imgs")
        private List<ImgsData> imgs;

        @SerializedName("is_wifi")
        private int isWifi;
        @SerializedName("is_wash")
        private int isWash;
        @SerializedName("is_day")
        private int isDay;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<ImgsData> getImgs() {
            return imgs;
        }

        public void setImgs(List<ImgsData> imgs) {
            this.imgs = imgs;
        }

        public int getIsWifi() {
            return isWifi;
        }

        public void setIsWifi(int isWifi) {
            this.isWifi = isWifi;
        }

        public int getIsWash() {
            return isWash;
        }

        public void setIsWash(int isWash) {
            this.isWash = isWash;
        }

        public int getIsDay() {
            return isDay;
        }

        public void setIsDay(int isDay) {
            this.isDay = isDay;
        }

        public static class ImgsData {
            @SerializedName("title")
            private String title;
            @SerializedName("url")
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
