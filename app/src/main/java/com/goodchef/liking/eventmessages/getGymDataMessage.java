package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.goodchef.liking.http.result.CoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 下午5:03
 */
public class getGymDataMessage extends BaseMessage{
    private CoursesResult.Courses.Gym mGym;

    public getGymDataMessage(CoursesResult.Courses.Gym gym) {
        mGym = gym;
    }

    public CoursesResult.Courses.Gym getGym() {
        return mGym;
    }
}
