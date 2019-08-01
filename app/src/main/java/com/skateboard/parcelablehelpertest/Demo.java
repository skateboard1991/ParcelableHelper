package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;

public class Demo implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Demo() {
    }

    protected Demo(Parcel in) {
    }

    public static final Parcelable.Creator<Demo> CREATOR = new Parcelable.Creator<Demo>() {
        @Override
        public Demo createFromParcel(Parcel source) {
            return new Demo(source);
        }

        @Override
        public Demo[] newArray(int size) {
            return new Demo[size];
        }
    };
}
