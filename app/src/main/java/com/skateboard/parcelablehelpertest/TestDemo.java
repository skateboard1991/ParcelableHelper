package com.skateboard.parcelablehelpertest;



import android.os.Parcel;


public class TestDemo implements android.os.Parcelable {


    public TestDemo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected TestDemo(Parcel in) {
    }

    public static final Creator<TestDemo> CREATOR = new Creator<TestDemo>() {
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
