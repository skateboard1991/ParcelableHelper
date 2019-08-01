package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import com.skateboard.parcelableannoation.Parcelable;

@Parcelable
public class ParcelableFatherObject {

    private String fatherName;

    public ParcelableFatherObject(String name){

    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
