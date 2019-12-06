package com.yuong.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        flowLayout = findViewById(R.id.flowLayout);
        flowLayout.setLineSpace(20);
        flowLayout.setColumnSpace(20);
    }
}
