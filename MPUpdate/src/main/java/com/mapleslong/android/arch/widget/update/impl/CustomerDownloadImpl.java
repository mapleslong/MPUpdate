package com.mapleslong.android.arch.widget.update.impl;

import android.content.Context;

import com.mapleslong.android.arch.widget.update.MPUpdateDownload;
import com.mapleslong.android.arch.widget.update.MPUpdateManager;
import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;
import com.mapleslong.android.arch.widget.update.network.AbsFileProgressCallback;
import com.mapleslong.android.arch.widget.update.utils.PathUtils;

/**
 * 创建时间: 2018/11/22
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class CustomerDownloadImpl implements IDownloadManager {
    private Context context;
    private int currentProgress;

    public CustomerDownloadImpl(Context context) {
        this.context = context;
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
    public void download(String title, String desc, String url, final MPUpdateManager.DownloadCallBack downloadCallBack) {
        //先取消之前的下载
        if (MPUpdateDownload.isDownloading()) {
            MPUpdateDownload.cancelAll();
            MPUpdateDownload.setIsDownloading(false);
        }
        String fileName = url.endsWith("apk") ? url.substring(url.lastIndexOf("/"), url.length()) : "update.apk";
        String filePath = PathUtils.getCachePath(context) + "/" + fileName;
        MPUpdateDownload.with().downloadPath(filePath).url(url).execute(new AbsFileProgressCallback() {
            @Override
            public void onStart() {
                if (downloadCallBack != null) {
                    downloadCallBack.onStart();
                }
                currentProgress = 0;
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
                }
                currentProgress = progress;

            }

            @Override
            public void onSuccess(String result) {
                if (downloadCallBack != null) {
                    downloadCallBack.onComplete(result);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (downloadCallBack != null) {
                    downloadCallBack.onFail(new Exception(errorMsg));
                }
            }

            @Override
            public void onCancel() {
                if (downloadCallBack != null) {
                    downloadCallBack.cancel();
                }
            }
        });
    }

    /**
     * 取消下载
     */
    @Override
    public void cancel() {
        MPUpdateDownload.cancelAll();
    }
}
