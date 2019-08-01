package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;


public class ParcelableSonObject  implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ParcelableSonObject(String name) {

    }

    public ParcelableSonObject(Parcel in) {
    }

    public static final Parcelable.Creator<ParcelableSonObject> CREATOR = new Parcelable.Creator<ParcelableSonObject>() {
        @Override
        public ParcelableSonObject createFromParcel(Parcel source) {
            return new ParcelableSonObject(source);
        }

        @Override
        public ParcelableSonObject[] newArray(int size) {
            return new ParcelableSonObject[size];
        }
    };
}
