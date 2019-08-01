package com.skateboard.parcelablehelpertest;

import android.os.Parcel;
import com.skateboard.parcelableannoation.Parcelable;

import java.util.List;

@Parcelable
public class ParcelableObject {



    private int id;

    private int[] ids;

    private List<Integer> idList;

    private String name;

    private String[] nameArray;

    private List<String> stringList;

    private ParcelableObject object;

    private ParcelableObject[] objectArray;

    private List<ParcelableObject> parcelableObjectList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNameArray() {
        return nameArray;
    }

    public void setNameArray(String[] nameArray) {
        this.nameArray = nameArray;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public ParcelableObject getObject() {
        return object;
    }

    public void setObject(ParcelableObject object) {
        this.object = object;
    }

    public ParcelableObject[] getObjectArray() {
        return objectArray;
    }

    public void setObjectArray(ParcelableObject[] objectArray) {
        this.objectArray = objectArray;
    }

    public List<ParcelableObject> getParcelableObjectList() {
        return parcelableObjectList;
    }

    public void setParcelableObjectList(List<ParcelableObject> parcelableObjectList) {
        this.parcelableObjectList = parcelableObjectList;
    }
}
