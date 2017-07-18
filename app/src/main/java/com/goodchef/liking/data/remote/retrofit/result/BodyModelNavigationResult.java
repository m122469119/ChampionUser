package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.data.remote.retrofit.result.data.BodyHistoryData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午12:08
 * version 1.0.0
 */

public class BodyModelNavigationResult extends LikingResult {


    @SerializedName("data")
    private HistoryTitleData mData;

    public HistoryTitleData getData() {
        return mData;
    }

    public void setData(HistoryTitleData data) {
        mData = data;
    }

    public static class HistoryTitleData extends Data{
        /**
         * column : weight
         * chinese_name : 体重
         * english_name :
         * unit : kg
         */

        @SerializedName("nav_data")
        private List<NavData> mNavData;
        /**
         * value : 50.00
         * body_time : 1
         */

        @SerializedName("history_data")
        private List<BodyHistoryData> mHistoryData;

        public List<NavData> getNavData() {
            return mNavData;
        }

        public void setNavData(List<NavData> navData) {
            mNavData = navData;
        }

        public List<BodyHistoryData> getHistoryData() {
            return mHistoryData;
        }

        public void setHistoryData(List<BodyHistoryData> historyData) {
            mHistoryData = historyData;
        }

        public static class NavData extends Data {
            @SerializedName("column")
            private String mColumn;
            @SerializedName("chinese_name")
            private String mChineseName;
            @SerializedName("english_name")
            private String mEnglishName;
            @SerializedName("unit")
            private String mUnit;
            private int id;
            private boolean isSelect;

            public String getColumn() {
                return mColumn;
            }

            public void setColumn(String column) {
                mColumn = column;
            }

            public String getChineseName() {
                return mChineseName;
            }

            public void setChineseName(String chineseName) {
                mChineseName = chineseName;
            }

            public String getEnglishName() {
                return mEnglishName;
            }

            public void setEnglishName(String englishName) {
                mEnglishName = englishName;
            }

            public String getUnit() {
                return mUnit;
            }

            public void setUnit(String unit) {
                mUnit = unit;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

    }
}
