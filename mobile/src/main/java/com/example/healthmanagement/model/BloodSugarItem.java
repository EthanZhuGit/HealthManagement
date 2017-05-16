package com.example.healthmanagement.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodSugarItem extends DataSupport implements Parcelable {

    @Column(unique = true)
    private Date date;
    private float beforeBreakfast;
    private float afterBreakfast;
    private float beforeLunch;
    private float afterLunch;
    private float beforeSupper;
    private float afterSupper;
    private float beforeSleep;
    private Date lastModifyDate;

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

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeFloat(this.beforeBreakfast);
        dest.writeFloat(this.afterBreakfast);
        dest.writeFloat(this.beforeLunch);
        dest.writeFloat(this.afterLunch);
        dest.writeFloat(this.beforeSupper);
        dest.writeFloat(this.afterSupper);
        dest.writeFloat(this.beforeSleep);
        dest.writeLong(this.lastModifyDate != null ? this.lastModifyDate.getTime() : -1);
    }

    public BloodSugarItem() {
    }

    protected BloodSugarItem(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.beforeBreakfast = in.readFloat();
        this.afterBreakfast = in.readFloat();
        this.beforeLunch = in.readFloat();
        this.afterLunch = in.readFloat();
        this.beforeSupper = in.readFloat();
        this.afterSupper = in.readFloat();
        this.beforeSleep = in.readFloat();
        long tmpLastModifyDate = in.readLong();
        this.lastModifyDate = tmpLastModifyDate == -1 ? null : new Date(tmpLastModifyDate);
    }

    public static final Creator<BloodSugarItem> CREATOR = new Creator<BloodSugarItem>() {
        @Override
        public BloodSugarItem createFromParcel(Parcel source) {
            return new BloodSugarItem(source);
        }

        @Override
        public BloodSugarItem[] newArray(int size) {
            return new BloodSugarItem[size];
        }
    };
}
