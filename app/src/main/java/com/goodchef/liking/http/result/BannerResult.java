package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 16/2/3.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class BannerResult extends LikingResult {

    @SerializedName("data")
    private BannerData mBannerData;

    public void setBannerData(BannerData bannerData) {
        this.mBannerData = bannerData;
    }

    public BannerData getBannerData() {
        return mBannerData;
    }

    public static class BannerData {
        /**
         * imgUrl : /img/banner/hello.jpg
         * loadUrl : http://www.chushi007.com
         * type : 0
         */

        @SerializedName("banner")
        private List<Banner> mBannerList;

        public void setBannerList(List<Banner> bannerList) {
            this.mBannerList = bannerList;
        }

        public List<Banner> getBannerList() {
            return mBannerList;
        }

        public static class Banner extends Data {
            @SerializedName("img_url")
            private String mImgUrl;
            @SerializedName("load_url")
            private String mLoadUrl;
            @SerializedName("load_type")
            private String mType;
            @SerializedName("title")
            private String title;

            public void setImgUrl(String imgUrl) {
                this.mImgUrl = imgUrl;
            }

            public void setLoadUrl(String loadUrl) {
                this.mLoadUrl = loadUrl;
            }

            public void setType(String type) {
                this.mType = type;
            }

            public String getImgUrl() {
                return mImgUrl;
            }

            public String getLoadUrl() {
                return mLoadUrl;
            }

            public String getType() {
                return mType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
