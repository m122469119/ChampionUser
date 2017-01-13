package com.goodchef.liking.http.result.data;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:16
 * version 1.0.0
 */

public class SportData {
    private String step_num;
    private String distance;
    private String kcal;
    private String bpm;
    private String create_time;

    public String getStep_num() {
        return step_num;
    }

    public void setStep_num(String step_num) {
        this.step_num = step_num;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
