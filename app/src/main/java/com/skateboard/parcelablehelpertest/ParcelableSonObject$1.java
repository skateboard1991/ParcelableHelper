package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import android.os.Parcelable;

final class ParcelableSonObject$1 implements Parcelable.Creator<ParcelableSonObject>
{
    @Override
    public ParcelableSonObject createFromParcel(final Parcel parcel) {
        return new ParcelableSonObject(parcel);
    }

    @Override
    public ParcelableSonObject[] newArray(final int n) {
        return new ParcelableSonObject[n];
    }
}