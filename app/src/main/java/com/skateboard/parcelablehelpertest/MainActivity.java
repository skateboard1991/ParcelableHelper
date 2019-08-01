package com.skateboard.parcelablehelpertest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SecondActivity.class);
        ParcelableObject demo = new ParcelableObject();
        demo.setId(1);
        demo.setIds(new int[]{1, 2});
        List<Integer> idList = new ArrayList<Integer>();
        idList.add(9);
        demo.setIdList(idList);
        demo.setName("hh");
        demo.setNameArray(new String[]{"1", "2", "3"});
        List<String> nameList = new ArrayList<>();
        demo.setNameList(nameList);
        demo.setObject(new ParcelableObject());
        demo.setObjectArray(new ParcelableObject[]{new ParcelableObject()});
        List<ParcelableObject> objectList = new ArrayList<>();
        objectList.add(new ParcelableObject());
        demo.setParcelableObjectList(objectList);
        intent.putExtra("DD", (Parcelable) demo);
        startActivity(intent);
    }

}
