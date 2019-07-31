package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;



public class ParcelableDemo{

    private String id;

    private boolean isRight;

    private long longId;

    private List<String> itemList;

    private String[] itemArray;

    private TestDemo[] testDemoArray;

    private List<TestDemo> testDemoList;

    @Nullable
    private TestDemo testDemo;

    @NonNull
    private TestDemo testDemo2;

    public ParcelableDemo() {
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.id);
//        dest.writeByte(this.isRight ? (byte) 1 : (byte) 0);
//        dest.writeLong(this.longId);
//        dest.writeStringList(this.itemList);
//        dest.writeStringArray(this.itemArray);
//        dest.writeTypedArray(this.testDemoArray, flags);
//        dest.writeTypedList(this.testDemoList);
//        dest.writeParcelable(this.testDemo, flags);
//    }
//
//    protected ParcelableDemo(Parcel in) {
//        this.id = in.readString();
//        this.isRight = in.readByte() != 0;
//        this.longId = in.readLong();
//        this.itemList = in.createStringArrayList();
//        this.itemArray = in.createStringArray();
//        this.testDemoArray = in.createTypedArray(TestDemo.CREATOR);
//        this.testDemoList = in.createTypedArrayList(TestDemo.CREATOR);
//        this.testDemo = in.readParcelable(TestDemo.class.getClassLoader());
//    }
//
//    public static final Creator<ParcelableDemo> CREATOR = new Creator<ParcelableDemo>() {
//        @Override
//        public ParcelableDemo createFromParcel(Parcel source) {
//            return new ParcelableDemo(source);
//        }
//
//        @Override
//        public ParcelableDemo[] newArray(int size) {
//            return new ParcelableDemo[size];
//        }
//    };
}
