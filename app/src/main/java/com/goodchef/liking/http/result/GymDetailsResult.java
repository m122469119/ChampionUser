package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 下午4:32
 */
public class GymDetailsResult extends LikingResult {
    @SerializedName("data")
    private GymDetailsData data;

    public GymDetailsData getData() {
        return data;
    }

    public void setData(GymDetailsData data) {
        this.data = data;
    }

    public static class GymDetailsData extends Data {
        @SerializedName("name")
        private String name;
        @SerializedName("announcement")
        private String announcement;
        @SerializedName("address")
        private String address;
        @SerializedName("imgs")
        private List<ImgsData> imgs;
        @SerializedName("tags")
        private List<TagData> mTagDataList;
        public List<TagData> getTagDataList() {
            return mTagDataList;
        }

        public void setTagDataList(List<TagData> tagDataList) {
            mTagDataList = tagDataList;
        }

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

        public static class ImgsData extends Data {
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

        public static class TagData extends Data {
            /**
             * name : 24小时
             * img_url : http://testapp.likingfit.com/images/appicon/icon_24.png
             */

            @SerializedName("name")
            private String name;
            @SerializedName("img_url")
            private String imgUrl;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }
}
