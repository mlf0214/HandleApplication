package com.example.handleapplication;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadActivity extends AppCompatActivity {

    private static final int DOWNLOAD_FAIL = 10002;
    private Button button;
    private ProgressBar progressBar;
    private Handler mHandler;
    private static final int DOWNLOAD_CODE=10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
        initEvent();
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download(
                                "https://releases.ubuntu.com/22.04/ubuntu-22.04.2-desktop-amd64.iso"
                        );
                    }
                }).start();
            }
        });
        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case DOWNLOAD_CODE:
                        progressBar.setProgress((Integer) msg.obj);
                        break;
                }
            }
        };
    }

    private void download(String ubuntuUrl) {
        InputStream stream=null;
        try {
            URL url=new URL(ubuntuUrl);
            URLConnection urlConnection= url.openConnection();
            stream = urlConnection.getInputStream();
            /*获取文件总长度
            * */
            int countLength=urlConnection.getContentLength();
            String downloadFilename= Environment.getExternalStoragePublicDirectory("Download")+ File.separator
                    +"ubuntu"+File.separator;
            File file=new File(downloadFilename);
            if (!file.exists()){
                file.mkdir();
            }
            String fileName = downloadFilename + "ubuntu.iso";
            File isoFile=new File(fileName);
            if (isoFile.exists()){
                isoFile.delete();
            }
            int downloadSize=0;
            byte[] bytes=new byte[1024];
            int length=0;
            OutputStream os=new FileOutputStream(fileName);
            while ((length=stream.read(bytes))!=-1){
                os.write(bytes,0,length);
                downloadSize+=length;
                //更新UI
                Message message=Message.obtain();
                message.obj=downloadSize*100/countLength;
                message.what=DOWNLOAD_CODE;
                mHandler.sendMessage(message);
            }
            stream.close();
            os.close();
        } catch (MalformedURLException e) {
//            Message message=Message.obtain();
//            message.what=DOWNLOAD_FAIL;
//            mHandler.sendMessage(message);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}