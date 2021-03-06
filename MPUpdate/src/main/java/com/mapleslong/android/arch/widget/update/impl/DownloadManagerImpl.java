package com.mapleslong.android.arch.widget.update.impl;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.mapleslong.android.arch.widget.update.MPUpdateDownload;
import com.mapleslong.android.arch.widget.update.MPUpdateManager;
import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;
import com.mapleslong.android.arch.widget.update.utils.PathUtils;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class DownloadManagerImpl implements IDownloadManager {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 下载分配的ID
     */
    private long downloadId;
    /**
     * 下载器
     */
    private DownloadManager downloadManager;

    public DownloadManagerImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 广播监听下载的各个状态
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                // 下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                // 下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                // 正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                // 下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    // 下载完成安装APK
                    installAPK();
                    break;
                // 下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "更新下载失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
        c.close();
    }

    /**
     * 下载到本地后执行安装
     */
    private void installAPK() {
        if (downloadManager == null) {
            return;
        }
        //获取下载文件的Uri
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
        if (downloadFileUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
            mContext.unregisterReceiver(receiver);
        }
    }

    /**
     * 开始下载
     *
     * @param title 下载标题
     * @param desc  下载内容描述
     * @param url   下载地址
     * @return
     */
    @Override
    public void download(String title, String desc, String url, MPUpdateManager.DownloadCallBack downloadCallBack) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游(默认是true)
        //request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(title);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        String fileName = url.endsWith("apk") ? url.substring(url.lastIndexOf("/"), url.length()) : "update.apk";
        //设置下载的路径
        request.setDestinationInExternalPublicDir(Environment.getDownloadCacheDirectory().getAbsolutePath(), fileName);
        //获取DownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    public void download(String title, String desc, String url) {
        download(title, desc, url, null);
    }

    /**
     * 取消下载
     */
    @Override
    public void cancel() {
        if (downloadManager != null) {
            downloadManager.remove(downloadId);
        }
    }
}
