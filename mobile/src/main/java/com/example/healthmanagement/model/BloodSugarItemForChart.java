package com.example.healthmanagement.model;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/19 0019.
 */

public class BloodSugarItemForChart {
    Date date;
    float beforeMealAvg;
    float afterMealAvg;
    float beforeDawn;
    float beforeSleep;

    public BloodSugarItemForChart(Date date, float beforeMealAvg, float afterMealAvg, float beforeDawn, float beforeSleep) {
        this.beforeMealAvg = beforeMealAvg;
        this.afterMealAvg = afterMealAvg;
        this.beforeDawn = beforeDawn;
        this.beforeSleep = beforeSleep;
        this.date=date;
    }

    public float getBeforeMealAvg() {
        return beforeMealAvg;
    }

    public float getAfterMealAvg() {
        return afterMealAvg;
    }

    public float getBeforeDawn() {
        return beforeDawn;
    }

    public float getBeforeSleep() {
        return beforeSleep;
    }

    public Date getDate() {
        return date;
    }
}
