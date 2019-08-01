package com.skateboard.parcelablehelpertest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author mac
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,SecondActivity.class);
        ParcelableSonObject demo=new ParcelableSonObject("hh");
        demo.setSonName("son");
        intent.putExtra("DD", (Parcelable) demo);
        startActivity(intent);
    }

}
