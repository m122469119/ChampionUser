package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.UserExerciseResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/11 下午3:17
 */
public interface UserExerciseView extends BaseView {
    void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData);
}
