package com.skateboard.parcelablehelpertest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);
        System.out.println("extras size is "+getIntent().getExtras().size());
        Demo demo=getIntent().getParcelableExtra("DD");
        System.out.println("demo name is "+demo.getName());
    }
}
