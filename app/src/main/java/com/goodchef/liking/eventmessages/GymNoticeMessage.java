package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.goodchef.liking.http.result.CoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/5 下午5:37
 */
public class GymNoticeMessage extends BaseMessage {
    private CoursesResult.Courses.Gym mGym;

    public GymNoticeMessage(CoursesResult.Courses.Gym gym) {
        mGym = gym;
    }

    public CoursesResult.Courses.Gym getGym() {
        return mGym;
    }

}
