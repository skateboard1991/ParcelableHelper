package com.skateboard.parcelablehelpertest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);
        ParcelableObject demo=getIntent().getParcelableExtra("DD");
        System.out.println("son name is "+demo.toString());
    }
}
