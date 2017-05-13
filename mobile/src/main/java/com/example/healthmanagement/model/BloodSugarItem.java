package com.example.healthmanagement.model;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodSugarItem extends DataSupport{

    @Column(unique = true)
    private Date date;
    private float beforeBreakfast;
    private float afterBreakfast;
    private float beforeLunch;
    private float afterLunch;
    private float beforeSupper;
    private float afterSupper;
    private float beforeSleep;

    /**
     *
     * @param beforeBreakfast 早餐前
     * @param afterBreakfast 早餐后
     * @param beforeLunch 午餐前
     * @param afterLunch 午餐后
     * @param beforeSupper 晚餐前
     * @param afterSupper 晚餐后
     * @param beforeSleep 睡前
     */
    public BloodSugarItem(float beforeBreakfast, float afterBreakfast,
                          float beforeLunch, float afterLunch, float beforeSupper,
                          float afterSupper, float beforeSleep) {

        this.beforeBreakfast = beforeBreakfast;
        this.afterBreakfast = afterBreakfast;
        this.beforeLunch = beforeLunch;
        this.afterLunch = afterLunch;
        this.beforeSupper = beforeSupper;
        this.afterSupper = afterSupper;
        this.beforeSleep = beforeSleep;
    }

    public BloodSugarItem(Date date, float beforeBreakfast, float afterBreakfast,
                          float beforeLunch, float afterLunch, float beforeSupper,
                          float afterSupper, float beforeSleep) {
        this.date = date;
        this.beforeBreakfast = beforeBreakfast;
        this.afterBreakfast = afterBreakfast;
        this.beforeLunch = beforeLunch;
        this.afterLunch = afterLunch;
        this.beforeSupper = beforeSupper;
        this.afterSupper = afterSupper;
        this.beforeSleep = beforeSleep;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getBeforeBreakfast() {
        return beforeBreakfast;
    }

    public float getAfterBreakfast() {
        return afterBreakfast;
    }

    public float getBeforeLunch() {
        return beforeLunch;
    }

    public float getAfterLunch() {
        return afterLunch;
    }

    public float getBeforeSupper() {
        return beforeSupper;
    }

    public float getAfterSupper() {
        return afterSupper;
    }

    public float getBeforeSleep() {
        return beforeSleep;
    }
}
