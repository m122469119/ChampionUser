package com.goodchef.liking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.module.gym.details.ArenaActivity;
import com.goodchef.liking.module.gym.list.ChangeGymActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;

/**
 * 作者： chengcheng
 * 时间： 17/9/21- 下午2:23
 * 描述：
 */

public class ChangeGymUtil {


    /**
     * 切换场馆
     */
    public static void changeGym(Context context, CoursesResult.Courses.Gym mGym, int index) {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            PopupUtils.showToast(context, context.getString(R.string.network_no_connection));
            return;
        }
        if (mGym != null && !StringUtils.isEmpty(mGym.getGymId()) && !StringUtils.isEmpty(mGym.getCityId())) {
            if (StringUtils.isEmpty(mGym.getCityName())) {
                UMengCountUtil.UmengCount(context, UmengEventId.CHANGE_GYM_ACTIVITY, "定位失败");
            } else {
                UMengCountUtil.UmengCount(context, UmengEventId.CHANGE_GYM_ACTIVITY, mGym.getCityName());
            }
            Intent intent = new Intent(context, ChangeGymActivity.class);
            intent.putExtra(LikingHomeActivity.KEY_SELECT_CITY_ID, mGym.getCityId());
            intent.putExtra(LikingHomeActivity.KEY_WHETHER_LOCATION, LikingHomeActivity.isWhetherLocation);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
            intent.putExtra(LikingHomeActivity.KEY_TAB_INDEX, index);
            context.startActivity(intent);
        } else {
            PopupUtils.showToast(context, context.getString(R.string.network_home_error));
        }
    }

    /***
     * 跳转门店
     */
    public static void jumpArenaActivity(Activity context, CoursesResult.Courses.Gym mGym) {
        if (mGym != null && !StringUtils.isEmpty(mGym.getGymId()) && !StringUtils.isEmpty(mGym.getName())) {
            UMengCountUtil.UmengCount(context, UmengEventId.ARENAACTIVITY);
            Intent intent = new Intent(context, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
        }
    }
}
