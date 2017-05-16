package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class BloodPressureItem extends DataSupport implements Parcelable {


    @Column(unique = true)
    private Date date;
    private float systolicPressure;
    private float diastolicPressure;
    private Date lastModifyDate;

    public BloodPressureItem() {

    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeFloat(this.systolicPressure);
        dest.writeFloat(this.diastolicPressure);
        dest.writeLong(this.lastModifyDate != null ? this.lastModifyDate.getTime() : -1);
    }

    protected BloodPressureItem(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.systolicPressure = in.readFloat();
        this.diastolicPressure = in.readFloat();
        long tmpLastModifyDate = in.readLong();
        this.lastModifyDate = tmpLastModifyDate == -1 ? null : new Date(tmpLastModifyDate);
    }

    public static final Creator<BloodPressureItem> CREATOR = new Creator<BloodPressureItem>() {
        @Override
        public BloodPressureItem createFromParcel(Parcel source) {
            return new BloodPressureItem(source);
        }

        @Override
        public BloodPressureItem[] newArray(int size) {
            return new BloodPressureItem[size];
        }
    };
}
