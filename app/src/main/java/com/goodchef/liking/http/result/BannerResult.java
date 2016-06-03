package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 16/2/3.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class BannerResult extends BaseResult {

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

        public static class Banner extends BaseData {
            @SerializedName("imgUrl")
            private String mImgUrl;
            @SerializedName("loadUrl")
            private String mLoadUrl;
            @SerializedName("type")
            private String mType;

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
        }
    }
}
