package com.skateboard.parcelablehelpertest;

import androidx.annotation.NonNull;
import com.skateboard.parcelableannoation.Ignore;
import com.skateboard.parcelableannoation.Parcelable;

import java.util.List;

@Parcelable
public class ParcelableObject {


    @Ignore
    private int id;

    private int[] ids;

    private List<Integer> idList;

    private String name;

    private String[] nameArray;

    private List<String> nameList;

    private Demo object;

    private Demo[] objectArray;

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

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public Demo getObject() {
        return object;
    }

    public void setObject(Demo object) {
        this.object = object;
    }

    public Demo[] getObjectArray() {
        return objectArray;
    }

    public void setObjectArray(Demo[] objectArray) {
        this.objectArray = objectArray;
    }

    public List<ParcelableObject> getParcelableObjectList() {
        return parcelableObjectList;
    }

    public void setParcelableObjectList(List<ParcelableObject> parcelableObjectList) {
        this.parcelableObjectList = parcelableObjectList;
    }


    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id is ").append(id)
                .append("ids length is ")
                .append(ids.length)
                .append("ids list size is ")
                .append(idList.size())
                .append("name is ").append(name)
                .append("namearray length is ")
                .append(nameArray.length).append("name list size is ")
                .append(nameList.size())
                .append("object is ").append(object)
                .append("object array length is").append(objectArray.length)
                .append("object list size is ").append(parcelableObjectList.size());
        return builder.toString();
    }



}
