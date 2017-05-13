package com.example.healthmanagement.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodPressureItem extends DataSupport{

    @Column(unique = true)
    private Date date;
    private float systolicPressure;
    private float diastolicPressure;

    /**
     *
     * @param date 记录时间
     * @param systolicPressure 收缩压
     * @param diastolicPressure 舒张压
     */



    public BloodPressureItem(Date date,  float systolicPressure, float diastolicPressure) {
        this.date = date;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public float getSystolicPpressure() {
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
}
