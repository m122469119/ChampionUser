package com.goodchef.liking.module.train;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.DateUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportWeekResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.util.*;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:55
 */

public class SportDataModel extends BaseModel {

    LinkedList<SportDataEntity> mSportDataEntities;

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public Observable<UserExerciseResult> getExerciseData() {
        return LikingNewApi.getInstance().getUserExerciseData(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<UserExerciseResult>applyHttpSchedulers());
    }

    public Observable<SportWeekResult> getSportStats() {
        return LikingNewApi.getInstance().getSportStatsData(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<SportWeekResult>applyHttpSchedulers())
                .doOnNext(new Consumer<SportWeekResult>() {
                    @Override
                    public void accept(SportWeekResult sportWeekResult) throws Exception {
                        createWeekDates(sportWeekResult);
                    }
                });
    }

    public Observable<SportListResult> getSportListResult(String page, String data) {
        return LikingNewApi.getInstance().getSportListResult(LikingNewApi.sHostVersion,
                LikingPreference.getToken(), page, data)
                .compose(RxUtils.<SportListResult>applyHttpSchedulers());
    }

    public SportDataModel() {
        mSportDataEntities = new LinkedList<>();
        initEntities();
    }

    private void initEntities() {
        long s = System.currentTimeMillis();
        for (int i = 7; i > 0; i --) {
            Date yyyyMMdd = new Date(s);
            mSportDataEntities.addFirst(new SportDataEntity(yyyyMMdd.getTime(),
                    DateUtils.formatDate("MM/dd", yyyyMMdd),
                    getWeek(yyyyMMdd.getTime()),
                    "0",
                    "0mins",
                    false));
            s -= 24 * 60 * 60 * 1000;
        }
    }


    private void createWeekDates(SportWeekResult sportWeekResult) {
        mSportDataEntities.clear();

        if (sportWeekResult == null
                || sportWeekResult.getData() == null
                || sportWeekResult.getData().getStats() == null
                || sportWeekResult.getData().getStats().size() == 0) {
            initEntities();
        } else {
            List<SportWeekResult.DataBean.StatsBean> stats = sportWeekResult.getData().getStats();
            long max = 0;
            for (SportWeekResult.DataBean.StatsBean bean : stats) {
                long s = Long.parseLong(bean.getSeconds());
                if (s > max) {
                    max = s;
                }
            }
            for (SportWeekResult.DataBean.StatsBean bean : stats) {
                long s = Long.parseLong(bean.getSeconds());
                float p = 0F ;
                if (max != 0) {
                    p =  (float) s / max;
                }
                Date yyyyMMdd = DateUtils.parseString("yyyyMMdd", bean.getDate());
                mSportDataEntities.add(new SportDataEntity(yyyyMMdd.getTime(),
                        DateUtils.formatDate("MM/dd", yyyyMMdd),
                        getWeek(yyyyMMdd.getTime()),
                        String.valueOf(p),
                        Integer.parseInt(bean.getSeconds()) / 60 + "mins",
                        false));
            }
        }
    }


    public SportDataEntity getDate4Index(int position) {
        if (mSportDataEntities == null || mSportDataEntities.size() <= position) {
            return null;
        }
        return mSportDataEntities.get(position);
    }

    private String getWeek(Long millis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(millis);
        int cWeek = instance.get(Calendar.DAY_OF_WEEK);
        String result = "";
        switch (cWeek) {
            case 1:
                result = "日";
                break;
            case 2:
                result = "一";
                break;
            case 3:
                result = "二";
                break;
            case 4:
                result = "三";
                break;
            case 5:
                result = "四";
                break;
            case 6:
                result = "五";
                break;
            case 7:
                result = "六";
                break;
        }
        return result;
    }


}
