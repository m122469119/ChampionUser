package com.goodchef.liking.http.result;


import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明: 自助团体课
 * Author : liking
 * Time: 下午12:26
 */

public class SelfHelpGroupCoursesResult extends BaseResult {


    @SerializedName("data")
    private SelfHelpGroupCoursesData mData;

    public SelfHelpGroupCoursesData getData() {
        return mData;
    }

    public void setData(SelfHelpGroupCoursesData data) {
        mData = data;
    }

    public static class SelfHelpGroupCoursesData {
        /**
         * date : 20161104
         * hour : [{"hour":"17:00","available":true,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"18:00","available":false,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"19:00","available":false,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"20:00","available":true,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"21:00","available":true,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"22:00","available":true,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]},{"hour":"23:00","available":true,"filled":true,"room":[{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]}]
         */

        @SerializedName("time")
        private List<TimeData> mTime;

        public List<TimeData> getTime() {
            return mTime;
        }

        public void setTime(List<TimeData> time) {
            mTime = time;
        }

        public static class TimeData {
            @SerializedName("date")
            private String mDate;
            /**
             * hour : 17:00
             * available : true
             * filled : true
             * room : [{"id":10001,"name":"101","capacity":100,"scheduled":false,"schedule_id":0}]
             */

            @SerializedName("hour")
            private List<HourData> mHour;

            public String getDate() {
                return mDate;
            }

            public void setDate(String date) {
                mDate = date;
            }

            public List<HourData> getHour() {
                return mHour;
            }

            public void setHour(List<HourData> hour) {
                mHour = hour;
            }

            public static class HourData extends BaseData{
                @SerializedName("hour")
                private String mHour;
                @SerializedName("available")
                private boolean mAvailable;
                @SerializedName("filled")
                private boolean mFilled;
                /**
                 * id : 10001
                 * name : 101
                 * capacity : 100
                 * scheduled : false
                 * schedule_id : 0
                 */

                @SerializedName("room")
                private List<RoomData> mRoom;

                public String getHour() {
                    return mHour;
                }

                public void setHour(String hour) {
                    mHour = hour;
                }

                public boolean isAvailable() {
                    return mAvailable;
                }

                public void setAvailable(boolean available) {
                    mAvailable = available;
                }

                public boolean isFilled() {
                    return mFilled;
                }

                public void setFilled(boolean filled) {
                    mFilled = filled;
                }

                public List<RoomData> getRoom() {
                    return mRoom;
                }

                public void setRoom(List<RoomData> room) {
                    mRoom = room;
                }

                public static class RoomData extends BaseData{
                    @SerializedName("id")
                    private int mId;
                    @SerializedName("name")
                    private String mName;
                    @SerializedName("capacity")
                    private int mCapacity;
                    @SerializedName("scheduled")
                    private boolean mScheduled;
                    @SerializedName("schedule_id")
                    private int mScheduleId;

                    public int getId() {
                        return mId;
                    }

                    public void setId(int id) {
                        mId = id;
                    }

                    public String getName() {
                        return mName;
                    }

                    public void setName(String name) {
                        mName = name;
                    }

                    public int getCapacity() {
                        return mCapacity;
                    }

                    public void setCapacity(int capacity) {
                        mCapacity = capacity;
                    }

                    public boolean isScheduled() {
                        return mScheduled;
                    }

                    public void setScheduled(boolean scheduled) {
                        mScheduled = scheduled;
                    }

                    public int getScheduleId() {
                        return mScheduleId;
                    }

                    public void setScheduleId(int scheduleId) {
                        mScheduleId = scheduleId;
                    }
                }
            }
        }
    }
}
