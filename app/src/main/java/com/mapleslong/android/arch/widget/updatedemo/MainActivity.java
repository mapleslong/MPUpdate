package com.mapleslong.android.arch.widget.updatedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapleslong.android.arch.widget.update.MPUpdateManager;
import com.mapleslong.android.arch.widget.update.impl.NotificationDownloadImpl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 较小的apk文件
     */
    private String url = "http://p.gdown.baidu.com/2c94946f0e26ae462034226fde89692a269f6bdf2aa6e9c8767d772124c9f7c7d260e6931fef78f570c881e16b38ef5c2078652c803b022fe844fb87d68c894ddbef8a9694a05f1e0ff373231ca712808c4440578d04145cdba6bc15f63a58840b47586cd429087098129921afed1197f1295b0f2cf49ff9250a04c0d4ef1ae92fdb5712c98e32f6be819bd5d37ee48e968923ef76a8cc7fb9742033e72b07c84dd4e14dc8d7931011e1242b1a867296948cf07eaea2a39a3dfd57541fe83eade2e5b448b9ba397e0a84fc90bc2405b969c9634da0803afc6b0464e4a7e333bb0c7072bd418e99312b5457043c33f156a13660aba0be6f15";
    /**
     * 梦幻花园游戏包体较大，可以很好的查看下载效果
     */
    private String url2 = "http://mhhy.dl.gxpan.cn/apk/ml/MBGYD092101/Gardenscapes-ledou-MBGYD092101.apk";

    private MPUpdateManager mpUpdateManager;
    Button btnCancel;
    Button btnDownloadManager;
    Button btnNotification;
    Button btnDefaultDialog;
    Button btnCustomerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mpUpdateManager = new MPUpdateManager();
    }

    private void initViews() {
        btnCancel = findViewById(R.id.btn_cancel);
        btnDownloadManager = findViewById(R.id.btn_downloadmanager);
        btnNotification = findViewById(R.id.btn_notification);
        btnDefaultDialog = findViewById(R.id.btn_defaultDialog);
        btnCustomerDialog = findViewById(R.id.btn_customerDialog);

        btnCancel.setOnClickListener(this);
        btnDownloadManager.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        btnDefaultDialog.setOnClickListener(this);
        btnCustomerDialog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                mpUpdateManager.cancel();
                break;
            case R.id.btn_downloadmanager:
                mpUpdateManager.downloadDownloadManager(this, "的撒旦", "dsadsadsa", url2);
                break;
            case R.id.btn_notification:
                mpUpdateManager.downloadNotificiation(this, "tongzhi", "dsadsaewrqewqew", url2, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, null);
                break;
            case R.id.btn_defaultDialog:
                mpUpdateManager.downloadDefaultDialog(this, "的撒旦", "dsadsadsa", url2);
                break;
            case R.id.btn_customerDialog:
                mpUpdateManager.downloadCustomer(this, "的撒旦", "dsadsadsa", url2, new MPUpdateManager.DownloadCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long readbyte) {

                    }

                    @Override
                    public void onComplete(String path) {

                    }

                    @Override
                    public void onFail(Exception e) {

                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            default:
                break;
        }
    }
}

