package com.iapppay.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvChannelMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvChannelMark = (TextView) findViewById(R.id.tv_channel_mark);
    }

    public void getChannelMark(View v) {


        tvChannelMark.setText("10086");
    }
}
