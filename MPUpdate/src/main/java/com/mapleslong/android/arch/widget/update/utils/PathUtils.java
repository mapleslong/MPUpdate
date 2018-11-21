package com.mapleslong.android.arch.widget.update.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class PathUtils {
    /**
     * 获取缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        return cachePath;
    }

    /**
     * 修改文件执行命令
     *
     * @param file
     */
    public static void changeApkFileMode(File file) {
        try {
            String cmd = "chmod -R 777 " + file.getParent();
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
