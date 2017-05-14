package com.example.healthmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyx10 on 2017/5/14 0014.
 */

public class IsCardShow implements Parcelable {
    private String name;
    private boolean isShow;

    public IsCardShow(String name, boolean isShow) {
        this.name = name;
        this.isShow = isShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isShow ? (byte) 1 : (byte) 0);
    }

    protected IsCardShow(Parcel in) {
        this.name = in.readString();
        this.isShow = in.readByte() != 0;
    }

    public static final Parcelable.Creator<IsCardShow> CREATOR = new Parcelable.Creator<IsCardShow>() {
        @Override
        public IsCardShow createFromParcel(Parcel source) {
            return new IsCardShow(source);
        }

        @Override
        public IsCardShow[] newArray(int size) {
            return new IsCardShow[size];
        }
    };
}
