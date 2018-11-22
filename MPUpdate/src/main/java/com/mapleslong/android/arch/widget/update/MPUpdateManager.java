package com.mapleslong.android.arch.widget.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.mapleslong.android.arch.widget.update.impl.DownloadManagerImpl;
import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;
import com.mapleslong.android.arch.widget.update.network.DownloadModel;

import java.io.File;

/**
 * 创建时间: 2018/11/21
 * 描述: 对外的主要工具类对象
 *
 * @author Mapleslong
 * @version 1.0
 */
public class MPUpdateManager {
    /**
     * 下载回调监听
     */
    public interface DownloadCallBack {
        void onStart();

        void onLoading(long total, long current);

        void onComplete(String path);

        void onFail(Exception e);

        void cancle();
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
    public static void downloadDownloadManager(Context context, DownloadModel model) {
        DownloadManagerImpl downloadManager = new DownloadManagerImpl(context);
        downloadManager.downloadAPK(model.getTitle(), model.getDesc(), model.getHttpUrl(), "");
    }

    /**
     * 开始通知栏的进度更新
     */
    public static void downloadNotificiation() {

    }

    /**
     * 开始磨人的对话框更新
     */
    public static void downloadDefaultDialog() {
    }

    /**
     * 开始自定义界面的更新
     */
    public static void downloadCustomerView() {

    }

    public static void cancel() {
        MPUpdateDownload.cancleAll();
    }
}
