package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;

public class DemoCreator implements Parcelable.Creator<Student> {

    @Override
    public Student createFromParcel(Parcel source) {
        return new Student(source);
    }

    @Override
    public Student[] newArray(int size) {
        return new Student[0];
    }
}
