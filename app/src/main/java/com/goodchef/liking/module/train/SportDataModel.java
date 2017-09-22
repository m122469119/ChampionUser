package com.goodchef.liking.module.train;


import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.DateUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportStatsResult;
import com.goodchef.liking.data.remote.retrofit.result.SportUserStatResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:55
 */

public class SportDataModel extends BaseModel {

    int typeTime = SportDataEntity.TYPE_TIME_DAY; ////type : 1 日 2 周 3 月

    LinkedList<SportDataEntity> mSportDataEntities;

    LinkedList<SportStatsResult.DataBean.StatsBean> mSportStatsResult;

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public StringBuilder sb = null;

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

    /**
     * 用户运动记录统计
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<SportUserStatResult> getSportUserStatsResult(String startDate, String endDate) {
        return LikingNewApi.getInstance().getSportUserStatsResult(LikingNewApi.sHostVersion,
                LikingPreference.getToken(), startDate, endDate)
                .compose(RxUtils.<SportUserStatResult>applyHttpSchedulers());
    }

    public SportDataModel(int type) {
        typeTime = type;
        mSportDataEntities = new LinkedList<>();
        mSportStatsResult = new LinkedList<>();
        sb = new StringBuilder();
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

            sb.setLength(0);
            String content = "";
            Date sDate = parseTime(bean.getStartDate());
            if (!isThisYear(sDate)) {
                sb.append(getYear(sDate) + "\n");
            }

            switch (typeTime) {
                case SportDataEntity.TYPE_TIME_DAY:
                    sb.append(DateUtils.formatDate("MM/dd", sDate));
                    content = getWeek(sTime);
                    break;
                case SportDataEntity.TYPE_TIME_WEEK:
                    Date endDate = parseTime(bean.getEndDate());
                    sb.append(DateUtils.formatDate("MM/dd", sDate));
                    sb.append("~");
                    sb.append(DateUtils.formatDate("MM/dd", endDate));
                    break;
                case SportDataEntity.TYPE_TIME_MONTH:
                    sb.append(DateUtils.formatDate("M", sDate));
                    sb.append("月");
                    break;
            }
            mSportDataEntities.add(new SportDataEntity(sTime, eTime,
                    sb.toString(), content,
                    String.valueOf(p),
                    Integer.parseInt(bean.getSeconds()) / 60 + "mins",
                    false));
        }
    }

    /**
     * 是否是今年
     *
     * @param date
     * @return
     */
    public boolean isThisYear(Date date) {
        try {
            return getYear(new Date()) == getYear(date);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断选择的日期是否是本周
     *
     * @param time
     * @return
     */
    public boolean isThisWeek(long time) {
        try {
            Calendar calendar = Calendar.getInstance();
            int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(new Date(time));
            int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            if (paramWeek == currentWeek) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断选择的日期是否是本月
     *
     * @param time
     * @return
     */
    public boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    private boolean isThisTime(long time, String pattern) {
        try {
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String param = sdf.format(date);//参数时间
            String now = sdf.format(new Date());//当前时间
            if (param.equals(now)) {
                return true;
            }else {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }

    public int getYear(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        } catch (Exception e) {
            return 0;
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
