package com.skateboard.parcelablehelpertest;


import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.AnyThread;

import java.util.*;

public class TestDemo implements Parcelable {

    private String name;

    private Student student;

    private boolean[] boArray;

    private Student[] studentArray;

    private List<Student> studentList;


    private List<Integer> intergerList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.student, flags);
        dest.writeBooleanArray(this.boArray);
        dest.writeTypedArray(this.studentArray, flags);
        dest.writeTypedList(this.studentList);
        dest.writeList(this.intergerList);
    }

    public TestDemo() {
    }

    protected TestDemo(Parcel in) {
        this.name = in.readString();
        this.student = in.readParcelable(Student.class.getClassLoader());
        this.boArray = in.createBooleanArray();
        this.studentArray = in.createTypedArray(Student.CREATOR);
        this.studentList = in.createTypedArrayList(Student.CREATOR);
        this.intergerList = new ArrayList<Integer>();
        in.readList(this.intergerList, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<TestDemo> CREATOR = new Parcelable.Creator<TestDemo>() {
        @Override
        public TestDemo createFromParcel(Parcel source) {
            return new TestDemo(source);
        }

        @Override
        public TestDemo[] newArray(int size) {
            return new TestDemo[size];
        }
    };
}
