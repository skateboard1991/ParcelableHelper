package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import com.skateboard.parcelableannoation.Parcelable;

@Parcelable
public class Demo {

    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
