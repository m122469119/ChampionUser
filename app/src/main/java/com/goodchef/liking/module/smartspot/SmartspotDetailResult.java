package com.goodchef.liking.module.smartspot;

import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmartspotDetailResult extends LikingResult {

    /**
     * data : {"info":{"title":"test","start_time":"1504264505","end_time":"1504264605"},"list":[{"reps":"1","time":100,"rest_time":30,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268801&Signature=CEoj7%2F8uZoJRlxrAX2uwcn7abkU%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268802&Signature=wtt2F0PlygQ0MqWnCnVMynXr2yU%3D"}},{"reps":"10","time":100,"rest_time":40,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36884_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268808&Signature=sTxedQKJYsytD%2BmwTxYzSfgtQBs%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36884.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268806&Signature=Z6TRWDfRfIbfPqRy4bl5QVjCM%2Bc%3D"}},{"reps":"8","time":100,"rest_time":0,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36886_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268818&Signature=Wmhd27BqFPCxb51vE%2FBzaQDgqNY%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36886.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268817&Signature=%2B1MQ8bbksqpzPoQytmx3SN2rofU%3D"}}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * info : {"title":"test","start_time":"1504264505","end_time":"1504264605"}
         * list : [{"reps":"1","time":100,"rest_time":30,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268801&Signature=CEoj7%2F8uZoJRlxrAX2uwcn7abkU%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268802&Signature=wtt2F0PlygQ0MqWnCnVMynXr2yU%3D"}},{"reps":"10","time":100,"rest_time":40,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36884_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268808&Signature=sTxedQKJYsytD%2BmwTxYzSfgtQBs%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36884.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268806&Signature=Z6TRWDfRfIbfPqRy4bl5QVjCM%2Bc%3D"}},{"reps":"8","time":100,"rest_time":0,"medias":{"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36886_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268818&Signature=Wmhd27BqFPCxb51vE%2FBzaQDgqNY%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36886.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268817&Signature=%2B1MQ8bbksqpzPoQytmx3SN2rofU%3D"}}]
         */

        @SerializedName("info")
        private InfoBean info;
        @SerializedName("list")
        private List<ListBean> list;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class InfoBean {
            /**
             * title : test
             * start_time : 1504264505
             * end_time : 1504264605
             */

            @SerializedName("title")
            private String title;
            @SerializedName("start_time")
            private String startTime;
            @SerializedName("end_time")
            private String endTime;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }
        }

        public static class ListBean {
            /**
             * reps : 1
             * time : 100
             * rest_time : 30
             * medias : {"img":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268801&Signature=CEoj7%2F8uZoJRlxrAX2uwcn7abkU%3D","video":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268802&Signature=wtt2F0PlygQ0MqWnCnVMynXr2yU%3D"}
             */

            @SerializedName("reps")
            private String reps;
            @SerializedName("time")
            private int time;
            @SerializedName("rest_time")
            private int restTime;
            @SerializedName("medias")
            private MediasBean medias;

            public String getReps() {
                return reps;
            }

            public void setReps(String reps) {
                this.reps = reps;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public int getRestTime() {
                return restTime;
            }

            public void setRestTime(int restTime) {
                this.restTime = restTime;
            }

            public MediasBean getMedias() {
                return medias;
            }

            public void setMedias(MediasBean medias) {
                this.medias = medias;
            }

            public static class MediasBean {
                /**
                 * img : http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883.mp4?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268801&Signature=CEoj7%2F8uZoJRlxrAX2uwcn7abkU%3D
                 * video : http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg?OSSAccessKeyId=LTAI9HqTxcb0M3Kw&Expires=1592268802&Signature=wtt2F0PlygQ0MqWnCnVMynXr2yU%3D
                 */

                @SerializedName("img")
                private String img;
                @SerializedName("video")
                private String video;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }
            }
        }
    }
}
