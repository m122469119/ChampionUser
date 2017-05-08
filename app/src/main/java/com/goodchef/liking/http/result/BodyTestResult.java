package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.goodchef.liking.http.result.data.BodyData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:00
 * version 1.0.0
 */

public class BodyTestResult extends LikingResult {


    /**
     * user_data : {"gender":"1","name":"qqq","height":"111","age":"26","avatar":"http://testimg.likingfit.com/img/2016/12/01/1480560043_583f8dab845b4_thumb.png"}
     * top_data : {"body_time":"2016-12-02 16:11:56","score":"1"}
     * body_analysis : {"body_data":[{"value":"120.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体重","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"骨骼肌","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体脂肪","english_name":"","unit":"kg"},{"value":"30.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"身体水分含量","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"去脂体重","english_name":"","unit":"kg"}],"advise":"","type":"body_analysis"}
     * fat_analysis : {"body_data":[{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"身体质量指数","english_name":"BMI","unit":"kg/㎡"},{"value":"10","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体脂百分比","english_name":"PBF","unit":"%"},{"value":"10","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"腰臀百分比","english_name":"WHR","unit":"%"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"基础代谢","english_name":"kcal","unit":"kj/㎡/h"}],"advise":"","type":"fat_analysis"}
     * muscle : {"body_data":[{"value":"10.00","evaluate":"1","chinese_name":"右上肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"左上肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"右下肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"左下肢节段肌肉","english_name":"","unit":"kg"}],"advise":"","type":"muscle"}
     * body_fat : {"body_data":[{"value":"1.00","evaluate":"1","chinese_name":"右上肢节段脂肪百分比","content":"10.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"左上肢节段脂肪百分比","content":"10.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"右下肢节段脂肪百分比","content":"1.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"左下肢节段脂肪百分比","content":"1.00kg","english_name":"","unit":"%"}],"advise":"","type":"body_fat"}
     * footer : {"body_data":[{"value":"10.00","chinese_name":"肌肉控制","english_name":"","unit":"kg"},{"value":"0","chinese_name":"推荐摄入卡路里量","english_name":"","unit":"kcal"},{"value":"10.00","chinese_name":"脂肪控制","english_name":"","unit":"kg"}],"type":"footer"}
     */

    @SerializedName("data")
    private BodyTestData mData;

    public BodyTestData getData() {
        return mData;
    }

    public void setData(BodyTestData data) {
        mData = data;
    }

    public static class BodyTestData {
        /**
         * gender : 1
         * name : qqq
         * height : 111
         * age : 26
         * avatar : http://testimg.likingfit.com/img/2016/12/01/1480560043_583f8dab845b4_thumb.png
         */

        @SerializedName("user_data")
        private UserDataData mUserData;
        /**
         * body_time : 2016-12-02 16:11:56
         * score : 1
         */

        @SerializedName("top_data")
        private TopDataData mTopData;
        /**
         * body_data : [{"value":"120.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体重","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"骨骼肌","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体脂肪","english_name":"","unit":"kg"},{"value":"30.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"身体水分含量","english_name":"","unit":"kg"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"去脂体重","english_name":"","unit":"kg"}]
         * advise :
         * type : body_analysis
         */

        @SerializedName("body_analysis")
        private BodyAnalysisData mBodyAnalysis;
        /**
         * body_data : [{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"身体质量指数","english_name":"BMI","unit":"kg/㎡"},{"value":"10","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"体脂百分比","english_name":"PBF","unit":"%"},{"value":"10","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"腰臀百分比","english_name":"WHR","unit":"%"},{"value":"10.00","criterion_max":"1.00","criterion_min":"1.00","chinese_name":"基础代谢","english_name":"kcal","unit":"kj/㎡/h"}]
         * advise :
         * type : fat_analysis
         */

        @SerializedName("fat_analysis")
        private FatAnalysisData mFatAnalysis;
        /**
         * body_data : [{"value":"10.00","evaluate":"1","chinese_name":"右上肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"左上肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"右下肢节段肌肉","english_name":"","unit":"kg"},{"value":"10.00","evaluate":"1","chinese_name":"左下肢节段肌肉","english_name":"","unit":"kg"}]
         * advise :
         * type : muscle
         */

        @SerializedName("muscle")
        private MuscleData mMuscle;
        /**
         * body_data : [{"value":"1.00","evaluate":"1","chinese_name":"右上肢节段脂肪百分比","content":"10.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"左上肢节段脂肪百分比","content":"10.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"右下肢节段脂肪百分比","content":"1.00kg","english_name":"","unit":"%"},{"value":"1.00","evaluate":"1","chinese_name":"左下肢节段脂肪百分比","content":"1.00kg","english_name":"","unit":"%"}]
         * advise :
         * type : body_fat
         */

        @SerializedName("body_fat")
        private BodyFatData mBodyFat;
        /**
         * body_data : [{"value":"10.00","chinese_name":"肌肉控制","english_name":"","unit":"kg"},{"value":"0","chinese_name":"推荐摄入卡路里量","english_name":"","unit":"kcal"},{"value":"10.00","chinese_name":"脂肪控制","english_name":"","unit":"kg"}]
         * type : footer
         */

        @SerializedName("footer")
        private FooterData mFooter;

        public UserDataData getUserData() {
            return mUserData;
        }

        public void setUserData(UserDataData userData) {
            mUserData = userData;
        }

        public TopDataData getTopData() {
            return mTopData;
        }

        public void setTopData(TopDataData topData) {
            mTopData = topData;
        }

        public BodyAnalysisData getBodyAnalysis() {
            return mBodyAnalysis;
        }

        public void setBodyAnalysis(BodyAnalysisData bodyAnalysis) {
            mBodyAnalysis = bodyAnalysis;
        }

        public FatAnalysisData getFatAnalysis() {
            return mFatAnalysis;
        }

        public void setFatAnalysis(FatAnalysisData fatAnalysis) {
            mFatAnalysis = fatAnalysis;
        }

        public MuscleData getMuscle() {
            return mMuscle;
        }

        public void setMuscle(MuscleData muscle) {
            mMuscle = muscle;
        }

        public BodyFatData getBodyFat() {
            return mBodyFat;
        }

        public void setBodyFat(BodyFatData bodyFat) {
            mBodyFat = bodyFat;
        }

        public FooterData getFooter() {
            return mFooter;
        }

        public void setFooter(FooterData footer) {
            mFooter = footer;
        }

        public static class UserDataData extends Data {
            @SerializedName("gender")
            private String mGender;
            @SerializedName("name")
            private String mName;
            @SerializedName("height")
            private String mHeight;
            @SerializedName("age")
            private String mAge;
            @SerializedName("avatar")
            private String mAvatar;

            public String getGender() {
                return mGender;
            }

            public void setGender(String gender) {
                mGender = gender;
            }

            public String getName() {
                return mName;
            }

            public void setName(String name) {
                mName = name;
            }

            public String getHeight() {
                return mHeight;
            }

            public void setHeight(String height) {
                mHeight = height;
            }

            public String getAge() {
                return mAge;
            }

            public void setAge(String age) {
                mAge = age;
            }

            public String getAvatar() {
                return mAvatar;
            }

            public void setAvatar(String avatar) {
                mAvatar = avatar;
            }
        }

        public static class TopDataData extends Data {
            @SerializedName("body_time")
            private String mBodyTime;
            @SerializedName("score")
            private String mScore;
            @SerializedName("title")
            private String title;
            @SerializedName("type")
            private String type;

            public String getBodyTime() {
                return mBodyTime;
            }

            public void setBodyTime(String bodyTime) {
                mBodyTime = bodyTime;
            }

            public String getScore() {
                return mScore;
            }

            public void setScore(String score) {
                mScore = score;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class BodyAnalysisData extends Data {
            @SerializedName("advise")
            private String mAdvise;
            @SerializedName("type")
            private String mType;
            @SerializedName("title")
            private String title;

            /**
             * value : 120.00
             * criterion_max : 1.00
             * criterion_min : 1.00
             * chinese_name : 体重
             * english_name :
             * unit : kg
             */

            @SerializedName("body_data")
            private List<BodyDataData> mBodyData;

            public String getAdvise() {
                return mAdvise;
            }

            public void setAdvise(String advise) {
                mAdvise = advise;
            }

            public String getType() {
                return mType;
            }

            public void setType(String type) {
                mType = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<BodyDataData> getBodyData() {
                return mBodyData;
            }

            public void setBodyData(List<BodyDataData> bodyData) {
                mBodyData = bodyData;
            }

            public static class BodyDataData extends Data {
                @SerializedName("value")
                private String mValue;
                @SerializedName("criterion_max")
                private String mCriterionMax;
                @SerializedName("criterion_min")
                private String mCriterionMin;
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

                public String getCriterionMax() {
                    return mCriterionMax;
                }

                public void setCriterionMax(String criterionMax) {
                    mCriterionMax = criterionMax;
                }

                public String getCriterionMin() {
                    return mCriterionMin;
                }

                public void setCriterionMin(String criterionMin) {
                    mCriterionMin = criterionMin;
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

        public static class FatAnalysisData extends Data {
            @SerializedName("advise")
            private String mAdvise;
            @SerializedName("type")
            private String mType;
            @SerializedName("title")
            private String title;

            /**
             * value : 10.00
             * criterion_max : 1.00
             * criterion_min : 1.00
             * chinese_name : 身体质量指数
             * english_name : BMI
             * unit : kg/㎡
             */

            @SerializedName("body_data")
            private List<BodyDataData> mBodyData;

            public String getAdvise() {
                return mAdvise;
            }

            public void setAdvise(String advise) {
                mAdvise = advise;
            }

            public String getType() {
                return mType;
            }

            public void setType(String type) {
                mType = type;
            }

            public List<BodyDataData> getBodyData() {
                return mBodyData;
            }

            public void setBodyData(List<BodyDataData> bodyData) {
                mBodyData = bodyData;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class BodyDataData {
                @SerializedName("value")
                private String mValue;
                @SerializedName("criterion_max")
                private String mCriterionMax;
                @SerializedName("criterion_min")
                private String mCriterionMin;
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

                public String getCriterionMax() {
                    return mCriterionMax;
                }

                public void setCriterionMax(String criterionMax) {
                    mCriterionMax = criterionMax;
                }

                public String getCriterionMin() {
                    return mCriterionMin;
                }

                public void setCriterionMin(String criterionMin) {
                    mCriterionMin = criterionMin;
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

        public static class MuscleData {
            @SerializedName("advise")
            private String mAdvise;
            @SerializedName("type")
            private String mType;

            @SerializedName("title")
            private String title;
            /**
             * value : 10.00
             * evaluate : 1
             * chinese_name : 右上肢节段肌肉
             * english_name :
             * unit : kg
             */

            @SerializedName("body_data")
            private List<BodyData> mBodyData;

            public String getAdvise() {
                return mAdvise;
            }

            public void setAdvise(String advise) {
                mAdvise = advise;
            }

            public String getType() {
                return mType;
            }

            public void setType(String type) {
                mType = type;
            }

            public List<BodyData> getBodyData() {
                return mBodyData;
            }

            public void setBodyData(List<BodyData> bodyData) {
                mBodyData = bodyData;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

        }
        public static class BodyFatData {
            @SerializedName("advise")
            private String mAdvise;
            @SerializedName("type")
            private String mType;
            @SerializedName("title")
            private String title;
            /**
             * value : 1.00
             * evaluate : 1
             * chinese_name : 右上肢节段脂肪百分比
             * content : 10.00kg
             * english_name :
             * unit : %
             */

            @SerializedName("body_data")
            private List<BodyData> mBodyData;

            public String getAdvise() {
                return mAdvise;
            }

            public void setAdvise(String advise) {
                mAdvise = advise;
            }

            public String getType() {
                return mType;
            }

            public void setType(String type) {
                mType = type;
            }

            public List<BodyData> getBodyData() {
                return mBodyData;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setBodyData(List<BodyData> bodyData) {
                mBodyData = bodyData;
            }


        }

        public static class FooterData {
            @SerializedName("type")
            private String mType;

            @SerializedName("title")
            private String title;

            /**
             * value : 10.00
             * chinese_name : 肌肉控制
             * english_name :
             * unit : kg
             */

            @SerializedName("body_data")
            private List<BodyDataData> mBodyData;

            public String getType() {
                return mType;
            }

            public void setType(String type) {
                mType = type;
            }

            public List<BodyDataData> getBodyData() {
                return mBodyData;
            }

            public void setBodyData(List<BodyDataData> bodyData) {
                mBodyData = bodyData;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class BodyDataData {
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
