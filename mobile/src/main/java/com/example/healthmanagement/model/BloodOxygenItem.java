package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodOxygenItem extends DataSupport implements Parcelable{

    @Column(unique = false)
    Date date;
    float number;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeFloat(this.number);
    }

    public BloodOxygenItem() {
    }

    protected BloodOxygenItem(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.number = in.readFloat();
    }

    public static final Creator<BloodOxygenItem> CREATOR = new Creator<BloodOxygenItem>() {
        @Override
        public BloodOxygenItem createFromParcel(Parcel source) {
            return new BloodOxygenItem(source);
        }

        @Override
        public BloodOxygenItem[] newArray(int size) {
            return new BloodOxygenItem[size];
        }
    };
}
