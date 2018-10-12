package com.iapppay.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.iapppay.channel.reader.AppChannelReader;

public class MainActivity extends AppCompatActivity {

    private TextView tvChannelMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvChannelMark = (TextView) findViewById(R.id.tv_channel_mark);
    }
    public void getChannelMark(View v) {
        String channel = AppChannelReader.get(getApplicationContext());
        if (TextUtils.isEmpty(channel)) {
            channel = "渠道标识符读取失败";
        }
        tvChannelMark.setText(channel);


    }

}
