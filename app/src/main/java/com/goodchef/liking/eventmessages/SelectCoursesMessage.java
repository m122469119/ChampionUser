package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:34
 */
public class SelectCoursesMessage extends BaseMessage{

    private SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData CoursesData;

    public SelectCoursesMessage(SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData) {
        CoursesData = coursesData;
    }

    public SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData getCoursesData() {
        return CoursesData;
    }
}
