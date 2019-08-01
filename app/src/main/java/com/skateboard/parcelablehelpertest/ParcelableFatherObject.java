package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import com.skateboard.parcelableannoation.Parcelable;

public class ParcelableFatherObject {

    private String fatherName;

    private Demo[] objectArray;

    public ParcelableFatherObject(String name) {

    }

    public ParcelableFatherObject(Parcel parcel) {

        objectArray = parcel.createTypedArray(Demo.CREATOR);


    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
