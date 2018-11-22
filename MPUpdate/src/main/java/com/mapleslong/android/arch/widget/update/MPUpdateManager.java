package com.mapleslong.android.arch.widget.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.mapleslong.android.arch.widget.update.impl.CustomerDownloadImpl;
import com.mapleslong.android.arch.widget.update.impl.DownloadManagerImpl;
import com.mapleslong.android.arch.widget.update.impl.NotificationDownloadImpl;
import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;

import java.io.File;

/**
 * 创建时间: 2018/11/21
 * 描述: 对外的主要工具类对象
 *
 * @author Mapleslong
 * @version 1.0
 */
public class MPUpdateManager {
    IDownloadManager downloadManager;

    /**
     * 下载回调监听
     */
    public interface DownloadCallBack {
        void onStart();

        void onLoading(long total, long readbyte);

        void onComplete(String path);

        void onFail(Exception e);

        void cancel();
    }

    private static final String AUTHORITIES = ".updateFileProvider";

    /**
     * 根据文件路径封装intent
     */
    public static Intent installIntent(Context context, String path) {
        Uri uri;
        File apkFile = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + AUTHORITIES, apkFile);
            //应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 封装web下载的intent
     */
    public static Intent download‌ByWeb(String downloadUrl) {
        Uri uri = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 开始系统自带的下载管理器进行下载
     */
    public void downloadDownloadManager(Context context, String title, String desc, String url) {
        downloadManager = new DownloadManagerImpl(context);
        downloadManager.download(title, desc, url, null);
    }

    /**
     * 开始通知栏的进度更新
     */
    public void downloadNotificiation(Context context, String title, String desc, String url, int smallIconRes, int largeIconRes, DownloadCallBack downloadCallBack) {
        resetDownloadManager();
        downloadManager = new NotificationDownloadImpl(context);
        ((NotificationDownloadImpl) downloadManager).setSmallIconRes(smallIconRes);
        ((NotificationDownloadImpl) downloadManager).setLargeIconRes(largeIconRes);
        downloadManager.download(title, desc, url, downloadCallBack);
    }

    private void resetDownloadManager() {
        if (downloadManager != null) {
            downloadManager.cancel();
            downloadManager = null;
        }
    }

    /**
     * 开始磨人的对话框更新
     */
    public void downloadDefaultDialog(final Context context, String title, String desc, String url) {
        resetDownloadManager();
        downloadManager = new CustomerDownloadImpl(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        downloadManager.download(title, desc, url, new DownloadCallBack() {
            @Override
            public void onStart() {
                dialog.setTitle("更新提示");
                dialog.setMessage("正在准备下载...");
                dialog.show();
            }

            @Override
            public void onLoading(long total, long readbyte) {
                dialog.setMessage("下载进度：" + (long) (readbyte * 100.0) / total + "%");
            }

            @Override
            public void onComplete(String path) {
                dialog.dismiss();
                installIntent(context, path);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
            }
        });
    }

    /**
     * 开始自定义界面的更新
     */
    public void downloadCustomer(Context context, String title, String desc, String url, DownloadCallBack downloadCallBack) {
        resetDownloadManager();
        downloadManager = new CustomerDownloadImpl(context);
        downloadManager.download(title, desc, url, downloadCallBack);
    }

    public void cancel() {
        if (downloadManager != null) {
            downloadManager.cancel();
        }
        MPUpdateDownload.cancelAll();
    }
}
