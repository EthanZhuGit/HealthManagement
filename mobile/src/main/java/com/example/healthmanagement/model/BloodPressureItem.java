package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodPressureItem extends DataSupport {


    @Column(unique = true)
    private Date date;
    private float systolicPressure;
    private float diastolicPressure;
    private Date lastModifyDate;
    private User user;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public float getSystolicPressure() {
        return systolicPressure;
    }

    public float getDiastolicPressure() {
        return diastolicPressure;
    }


    public void setSystolicPressure(float systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public void setDiastolicPressure(float diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
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
