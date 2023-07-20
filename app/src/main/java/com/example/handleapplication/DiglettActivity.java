package com.example.handleapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Random;

public class DiglettActivity extends AppCompatActivity {

    private static final int CODE = 1001;
    private ImageView imageView;
    private TextView textView;
    private Button beginGame;
    private int mTotalCount;
    private int mSuccessCount;
    private Handler mHandler;
    public static final int MAX_COUNT=10;
    private int[][] mPosition=new int[][]{
            {342,180},{342,620},
            {342,280}, {142,120},
            {142,480},{542,120}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diglett);
        initView();
        initData();
    }

    private void initData() {
        beginGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setVisibility(View.GONE);
                mSuccessCount++;
                textView.setText("打中了"+mSuccessCount+"只;共"+MAX_COUNT+"只");
                return false;
            }
        });
    }

    private void start() {
        beginGame.setEnabled(false);
        beginGame.setText("游戏中");
        //发送消息
        next(0);
    }

    private void next(int delayTime) {
        int postsion=new Random().nextInt(mPosition.length);
        Message message=Message.obtain();
        message.what=CODE;
        message.arg1=postsion;
        mHandler.sendMessageDelayed(message,delayTime);
        mTotalCount++;

    }

    public static class DiglettiHandler extends Handler{
        public final WeakReference<DiglettActivity> activityWeakReference;

        public DiglettiHandler(DiglettActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DiglettActivity diglettActivity = activityWeakReference.get();
            if (msg.what==CODE){
                if (diglettActivity.mSuccessCount>=MAX_COUNT){
                    diglettActivity.clear();
                    Toast.makeText(diglettActivity, "地鼠打完了", Toast.LENGTH_SHORT).show();
                    diglettActivity.clear();
                    return;
                }
                int postion = msg.arg1;
                diglettActivity.imageView.setX(diglettActivity.mPosition[postion][0]);
                diglettActivity.imageView.setY(diglettActivity.mPosition[postion][1]);
                diglettActivity.imageView.setVisibility(View.VISIBLE);
                int randomTime=new Random().nextInt(500)+500;
                diglettActivity.next(randomTime);
            }
        }
    }

    private void clear() {
        mTotalCount=0;
        mSuccessCount=0;
        imageView.setVisibility(View.GONE);
        beginGame.setText("点击开始");
        beginGame.setEnabled(true);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.text_View);
        beginGame = (Button) findViewById(R.id.begin_game);
        mHandler=new DiglettiHandler(this);
    }
}