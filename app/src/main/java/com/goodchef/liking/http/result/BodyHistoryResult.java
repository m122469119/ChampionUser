package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:23
 * version 1.0.0
 */

public class BodyHistoryResult extends BaseResult {


    @SerializedName("data")
    private BodyHistoryData mData;

    public BodyHistoryData getData() {
        return mData;
    }

    public void setData(BodyHistoryData data) {
        mData = data;
    }

    public static class BodyHistoryData {
        /**
         * body_id : 1
         * score : 1
         * bmi : {"value":"1.00","chinese_name":"身体质量指数","english_name":"BMI","unit":"kg/m²"}
         * fat_rate : {"value":"1","chinese_name":"体脂百分比","english_name":"PBF","unit":"%"}
         * waist_hip : {"value":"1","chinese_name":"腰臀百分比","english_name":"WHR","unit":"%"}
         * body_time : 1
         */

        @SerializedName("list")
        private List<ListData> mList;

        public List<ListData> getList() {
            return mList;
        }

        public void setList(List<ListData> list) {
            mList = list;
        }

        public static class ListData {
            @SerializedName("body_id")
            private String mBodyId;
            @SerializedName("score")
            private String mScore;
            /**
             * value : 1.00
             * chinese_name : 身体质量指数
             * english_name : BMI
             * unit : kg/m²
             */

            @SerializedName("bmi")
            private BmiData mBmi;
            /**
             * value : 1
             * chinese_name : 体脂百分比
             * english_name : PBF
             * unit : %
             */

            @SerializedName("fat_rate")
            private FatRateData mFatRate;
            /**
             * value : 1
             * chinese_name : 腰臀百分比
             * english_name : WHR
             * unit : %
             */

            @SerializedName("waist_hip")
            private WaistHipData mWaistHip;
            @SerializedName("body_time")
            private String mBodyTime;

            public String getBodyId() {
                return mBodyId;
            }

            public void setBodyId(String bodyId) {
                mBodyId = bodyId;
            }

            public String getScore() {
                return mScore;
            }

            public void setScore(String score) {
                mScore = score;
            }

            public BmiData getBmi() {
                return mBmi;
            }

            public void setBmi(BmiData bmi) {
                mBmi = bmi;
            }

            public FatRateData getFatRate() {
                return mFatRate;
            }

            public void setFatRate(FatRateData fatRate) {
                mFatRate = fatRate;
            }

            public WaistHipData getWaistHip() {
                return mWaistHip;
            }

            public void setWaistHip(WaistHipData waistHip) {
                mWaistHip = waistHip;
            }

            public String getBodyTime() {
                return mBodyTime;
            }

            public void setBodyTime(String bodyTime) {
                mBodyTime = bodyTime;
            }

            public static class BmiData {
                @SerializedName("value")
                private String mValue;
                @SerializedName("chinese_name")
                private String mChineseName;
                @SerializedName("english_name")
                private String mEnglishName;
                @SerializedName("unit")
                private String mUnit;

                public String getValue() {
                    return mValue;
                }

                public void setValue(String value) {
                    mValue = value;
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
            }

            public static class FatRateData {
                @SerializedName("value")
                private String mValue;
                @SerializedName("chinese_name")
                private String mChineseName;
                @SerializedName("english_name")
                private String mEnglishName;
                @SerializedName("unit")
                private String mUnit;

                public String getValue() {
                    return mValue;
                }

                public void setValue(String value) {
                    mValue = value;
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
            }

            public static class WaistHipData {
                @SerializedName("value")
                private String mValue;
                @SerializedName("chinese_name")
                private String mChineseName;
                @SerializedName("english_name")
                private String mEnglishName;
                @SerializedName("unit")
                private String mUnit;

                public String getValue() {
                    return mValue;
                }

                public void setValue(String value) {
                    mValue = value;
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
            }
        }
    }
}
