package com.goodchef.liking.module.train;


import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.DateUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportStatsResult;
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

    public final static int TYPE_TIME_DAY = 1;
    public final static int TYPE_TIME_WEEK = 2;
    public final static int TYPE_TIME_MONTH = 3;

    int typeTime = TYPE_TIME_DAY; ////type : 1 日 2 周 3 月

    LinkedList<SportDataEntity> mSportDataEntities;

    LinkedList<SportStatsResult.DataBean.StatsBean> mSportStatsResult;

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public Observable<UserExerciseResult> getExerciseData() {
        return LikingNewApi.getInstance().getUserExerciseData(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<UserExerciseResult>applyHttpSchedulers());
    }

    public Observable<SportStatsResult> getSportStats() {
        Date date;
        if (mSportStatsResult != null && mSportStatsResult.size() > 0 && mSportStatsResult.getLast() != null) {
            date = parseTime(mSportStatsResult.getLast().getStartDate());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
        } else {
            date = new Date();
        }
        String currentDate = DateUtils.formatDate("yyyyMMdd", date);

        return LikingNewApi.getInstance().getSportStatsData(LikingNewApi.sHostVersion, LikingPreference.getToken(), String.valueOf(typeTime), currentDate)
                .compose(RxUtils.<SportStatsResult>applyHttpSchedulers())
                .doOnNext(new Consumer<SportStatsResult>() {
                    @Override
                    public void accept(SportStatsResult result) throws Exception {
                        if (result.getData() != null && result.getData().getStats() != null) {
                            mSportStatsResult.addAll(result.getData().getStats());
                            createWeekDates(mSportStatsResult);
                        }
                    }
                });
    }

    public Observable<SportListResult> getSportListResult(String page, String data) {
        return LikingNewApi.getInstance().getSportListResult(LikingNewApi.sHostVersion,
                LikingPreference.getToken(), page, data)
                .compose(RxUtils.<SportListResult>applyHttpSchedulers());
    }

    public SportDataModel(int type) {
        typeTime = type;
        mSportDataEntities = new LinkedList<>();
        mSportStatsResult = new LinkedList<>();
//        initEntities();
    }

    private void initEntities() {
        long s = System.currentTimeMillis();
        for (int i = 7; i > 0; i--) {
            Date yyyyMMdd = new Date(s);
            mSportDataEntities.addFirst(new SportDataEntity(yyyyMMdd.getTime(),
                    yyyyMMdd.getTime(),
                    DateUtils.formatDate("MM/dd", yyyyMMdd),
                    getWeek(yyyyMMdd.getTime()),
                    "0",
                    "0mins",
                    false));
            s -= 24 * 60 * 60 * 1000;
        }
    }


    private void createWeekDates(List<SportStatsResult.DataBean.StatsBean> datas) {
        mSportDataEntities.clear();

        List<SportStatsResult.DataBean.StatsBean> stats = datas;
        long max = 0;
        for (SportStatsResult.DataBean.StatsBean bean : stats) {
            long s = Long.parseLong(bean.getSeconds());
            if (s > max) {
                max = s;
            }
        }

        for (SportStatsResult.DataBean.StatsBean bean : stats) {
            long s = Long.parseLong(bean.getSeconds());
            float p = 0F;
            if (max != 0) {
                p = (float) s / max;
            }

            Long sTime = parseTime(bean.getStartDate()).getTime();
            Long eTime = parseTime(bean.getEndDate()).getTime();

            String title = "";
            String content = "";
            switch (typeTime) {
                case TYPE_TIME_DAY:
                    title = DateUtils.formatDate("MM/dd", parseTime(bean.getStartDate()));
                    content = getWeek(sTime);
                    break;
                case TYPE_TIME_WEEK:
                    title = DateUtils.formatDate("MM/dd", parseTime(bean.getStartDate()))
                            + " " + DateUtils.formatDate("MM/dd", parseTime(bean.getEndDate()));
                    break;
                case TYPE_TIME_MONTH:
                    title = DateUtils.formatDate("MM", parseTime(bean.getStartDate())) + "月";
                    break;
            }
            mSportDataEntities.add(new SportDataEntity(sTime, eTime,
                    title, content,
                    String.valueOf(p),
                    Integer.parseInt(bean.getSeconds()) / 60 + "mins",
                    false));
        }
    }

    private Date parseTime(String time) {
        try {
            return DateUtils.parseString("yyyyMMdd", time);
        } catch (Exception e) {
            return new Date();
        }
    }

    public SportDataEntity getDate4Index(int position) {
        if (mSportDataEntities == null || mSportDataEntities.size() <= position) {
            return null;
        }
        return mSportDataEntities.get(position);
    }

    public List<SportDataEntity> getSportDatas() {
        return mSportDataEntities;
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
