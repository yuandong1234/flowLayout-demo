package com.yuong.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String[] titles = {
            "华春莹怒批美政客涉疆言论：他们信口雌黄地编造着世纪谎言，应该感到羞耻",
            "华春莹怒批美政客涉疆言论：",
            "他们信口雌黄地编造着世纪谎言",
            "应该感到羞耻",
            "羞耻",
            "华春莹怒批美政客涉疆言",
            "华春莹怒批美政客涉疆言论：他们信口雌黄地编造着世纪谎言，应该感到羞耻"
    };

    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        flowLayout = findViewById(R.id.flowLayout);
        flowLayout.setLineSpace(20);
        flowLayout.setColumnSpace(20);
    }

    private void initData() {
        for (String temp : titles) {
            View view = View.inflate(this, R.layout.layout_item, null);
            TextView title = view.findViewById(R.id.title);
            title.setText(temp);
            flowLayout.addView(view);
        }
    }
}
