package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import com.skateboard.parcelableannoation.Parcelable;


@Parcelable
public class ParcelableSonObject extends ParcelableFatherObject{


    private String sonName;

    public ParcelableSonObject(String name) {
        super(name);
    }

    public String getSonName() {
        return sonName;
    }

    public void setSonName(String sonName) {
        this.sonName = sonName;
    }
}
