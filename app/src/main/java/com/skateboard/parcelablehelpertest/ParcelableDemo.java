package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableDemo implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ParcelableDemo() {
    }

    protected ParcelableDemo(Parcel in) {
    }

    public static final Parcelable.Creator<ParcelableDemo> CREATOR = new Parcelable.Creator<ParcelableDemo>() {
        @Override
        public ParcelableDemo createFromParcel(Parcel source) {
            return new ParcelableDemo(source);
        }

        @Override
        public ParcelableDemo[] newArray(int size) {
            return new ParcelableDemo[size];
        }
    };
}

