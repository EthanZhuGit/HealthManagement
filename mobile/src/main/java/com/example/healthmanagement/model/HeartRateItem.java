package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class HeartRateItem extends DataSupport implements Parcelable{

    @Column(unique = true)
    Date date;
    int rate;


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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.rate);
    }

    protected HeartRateItem(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.rate = in.readInt();
    }

    public static final Creator<HeartRateItem> CREATOR = new Creator<HeartRateItem>() {
        @Override
        public HeartRateItem createFromParcel(Parcel source) {
            return new HeartRateItem(source);
        }

        @Override
        public HeartRateItem[] newArray(int size) {
            return new HeartRateItem[size];
        }
    };
}
