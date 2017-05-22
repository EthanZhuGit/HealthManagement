package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class HeartRateItem extends DataSupport  {

    @Column(unique = true)
    private Date date;
    private int rate;
    private User user;
    @Column(defaultValue = "false")
    private boolean isCloudStorage;
    private String  object_id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCloudStorage() {
        return isCloudStorage;
    }

    public void setCloudStorage(boolean cloudStorage) {
        isCloudStorage = cloudStorage;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }
}
