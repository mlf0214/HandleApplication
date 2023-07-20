package com.example.handleapplication;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;

//倒计时实现
public class MainActivity extends Activity {

    private TextView mTextView;
    private static final int COUNTDOWN_TIME_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        Handler handler=new CountDownTimeHandler(this);
        Message message=Message.obtain();
        message.arg1= 10;
        message.what= COUNTDOWN_TIME_CODE;

        handler.sendMessageDelayed(message, 1000);
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.textView);
    }
    public static class CountDownTimeHandler extends Handler{
        public final WeakReference<MainActivity> weakReference;

        public CountDownTimeHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity =weakReference.get();
            if (msg.what == COUNTDOWN_TIME_CODE) {
                int value = msg.arg1;
                activity.mTextView.setText(String.valueOf(value--));
                if (value >= 0) {
                    Message message = Message.obtain();
                    message.what = COUNTDOWN_TIME_CODE;
                    message.arg1 = value;
                    sendMessageDelayed(message, 1000);
                }
                if (value==-1){
                    activity.mTextView.setText("停止");
                }
            }
        }
    }
}