package com.mapleslong.android.arch.widget.update.impl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.mapleslong.android.arch.widget.update.MPUpdateDownload;
import com.mapleslong.android.arch.widget.update.MPUpdateManager;
import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;
import com.mapleslong.android.arch.widget.update.network.AbsFileProgressCallback;
import com.mapleslong.android.arch.widget.update.utils.PathUtils;


/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class NotificationDownloadImpl implements IDownloadManager {
    private Context mContext;
    private int notifyId = 7575;
    private String CHANNEL_NAME = "download_channel";
    private static NotificationCompat.Builder builder;
    private static NotificationManager manager;
    private static NotificationChannel channel;
    int currentProgress = 0;
    private int smallIconRes = 0;
    private int largeIconRes = 0;

    public NotificationDownloadImpl(Context context) {
        this.mContext = context;
    }


    public int getSmallIconRes() {
        return smallIconRes;
    }

    public void setSmallIconRes(int smallIconRes) {
        this.smallIconRes = smallIconRes;
    }

    public int getLargeIconRes() {
        return largeIconRes;
    }

    public void setLargeIconRes(int largeIconRes) {
        this.largeIconRes = largeIconRes;
    }

    /**
     * 关闭通知
     */
    private void closeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //关闭通知通道
            manager.deleteNotificationChannel(String.valueOf(notifyId));
        }
        manager.cancel(notifyId);
    }


    /**
     * 开始下载
     *
     * @param title            下载标题
     * @param desc             下载内容描述
     * @param url              下载地址
     * @param downloadCallBack 回调方法
     */
    @Override
    public void download(final String title, final String desc, String url, final MPUpdateManager.DownloadCallBack downloadCallBack) {
        //先取消之前的下载
        if (MPUpdateDownload.isDownloading()) {
            MPUpdateDownload.cancelAll();
            MPUpdateDownload.setIsDownloading(false);
        }
        String fileName = url.endsWith("apk") ? url.substring(url.lastIndexOf("/"), url.length()) : "update.apk";
        String filePath = PathUtils.getCachePath(mContext) + "/" + fileName;
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(String.valueOf(notifyId), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            //是否在桌面icon右上角展示小红点
            channel.enableLights(true);
            //小红点颜色
            channel.setLightColor(Color.RED);
            //是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);
            builder = new NotificationCompat.Builder(mContext, String.valueOf(notifyId));
            manager.createNotificationChannel(channel);
        } else {
            builder = new NotificationCompat.Builder(mContext);
        }

        MPUpdateDownload.with().downloadPath(filePath).url(url).execute(new AbsFileProgressCallback() {
            @Override
            public void onStart() {
                if (downloadCallBack != null) {
                    downloadCallBack.onStart();
                }
                MPUpdateDownload.setIsDownloading(true);
                currentProgress = 0;
                builder.setContentTitle(title)
                        .setContentText(desc)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(getSmallIconRes())
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getLargeIconRes()))
                        .setProgress(100, 0, false);
                manager.notify(notifyId, builder.build());
            }

            @Override
            public void onProgress(long bytesRead, long contentLength, boolean done) {
                MPUpdateDownload.setIsDownloading(true);
                //计算进度
                int progress = (int) (bytesRead * 100 / contentLength);
                //只有进度+1才回调，防止过快
                if (progress - currentProgress >= 1) {
                    if (downloadCallBack != null) {
                        downloadCallBack.onLoading(contentLength, bytesRead);
                    }
                    builder.setProgress(100, progress, false);
                    builder.setContentText("下载进度: " + progress + "%");
                    manager.notify(notifyId, builder.build());
                }
                currentProgress = progress;
            }

            @Override
            public void onSuccess(String result) {
                MPUpdateDownload.setIsDownloading(false);
                if (downloadCallBack != null) {
                    downloadCallBack.onComplete(result);
                }
                MPUpdateManager.installIntent(mContext, result);
                closeNotification();
            }

            @Override
            public void onFailed(String errorMsg) {
                MPUpdateDownload.setIsDownloading(false);
                builder.setContentText("下载失败");
                if (downloadCallBack != null) {
                    downloadCallBack.onFail(new Exception(errorMsg));
                }
                closeNotification();
            }

            @Override
            public void onCancel() {
                MPUpdateDownload.setIsDownloading(false);
                if (downloadCallBack != null) {
                    downloadCallBack.cancel();
                }
                closeNotification();
            }
        });
    }


    @Override
    public void cancel() {
        MPUpdateDownload.cancelAll();
        closeNotification();
    }

}
