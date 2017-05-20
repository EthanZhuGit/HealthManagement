package com.example.healthmanagement.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodSugarItem extends DataSupport  {

    @Column(unique = true)
    private Date date;
    private float beforeBreakfast;
    private float afterBreakfast;
    private float beforeLunch;
    private float afterLunch;
    private float beforeSupper;
    private float afterSupper;
    private float beforeSleep;
    private float beforeDawn;
    private Date lastModifyDate;
    private User user;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getBeforeBreakfast() {
        return beforeBreakfast;
    }

    public void setBeforeBreakfast(float beforeBreakfast) {
        this.beforeBreakfast = beforeBreakfast;
    }

    public float getAfterBreakfast() {
        return afterBreakfast;
    }

    public void setAfterBreakfast(float afterBreakfast) {
        this.afterBreakfast = afterBreakfast;
    }

    public float getBeforeLunch() {
        return beforeLunch;
    }

    public void setBeforeLunch(float beforeLunch) {
        this.beforeLunch = beforeLunch;
    }

    public float getAfterLunch() {
        return afterLunch;
    }

    public void setAfterLunch(float afterLunch) {
        this.afterLunch = afterLunch;
    }

    public float getBeforeSupper() {
        return beforeSupper;
    }

    public void setBeforeSupper(float beforeSupper) {
        this.beforeSupper = beforeSupper;
    }

    public float getAfterSupper() {
        return afterSupper;
    }

    public void setAfterSupper(float afterSupper) {
        this.afterSupper = afterSupper;
    }

    public float getBeforeSleep() {
        return beforeSleep;
    }

    public void setBeforeSleep(float beforeSleep) {
        this.beforeSleep = beforeSleep;
    }

    public float getBeforeDawn() {
        return beforeDawn;
    }

    public void setBeforeDawn(float beforeDawn) {
        this.beforeDawn = beforeDawn;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
