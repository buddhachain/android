package com.chain.buddha.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;


/**
 * @author cross_ly DATE 2019/04/23
 * <p>描述:常量值
 */
public class FileUtils {

    /**
     * 文件存储相关常量
     */
    public static class SDCardConstants {

        private static final String TAG = "SDCardConstants";
        /**
         * 转码文件后缀
         */
        public final static String TRANSCODE_SUFFIX = ".mp4_transcode";

        /**
         * 裁剪文件后缀
         */
        public final static String CROP_SUFFIX = "-crop.mp4";

        /**
         * 合成文件后缀
         */
        public final static String COMPOSE_SUFFIX = "-compose.mp4";

        /**
         * 裁剪 & 录制 & 转码输出文件的目录
         * android Q 版本默认路径
         * /storage/emulated/0/Android/data/包名/files/Media/
         * android Q 以下版本默认"/sdcard/DCIM/Camera/"
         */
        public static String getDir(Context context) {
            String dir;
            dir = context.getExternalFilesDir("").getPath();
            return dir;
        }

        /**
         * 获取外部缓存目录 版本默认"/storage/emulated/0/Android/data/包名/file/Cache"
         *
         * @param context Context
         * @return string path
         */
        public static String getCacheDir(Context context) {
            File cacheDir = context.getExternalCacheDir();
            return cacheDir == null ? "" : cacheDir.getPath();
        }

        /**
         * 清空外部缓存目录文件 "/storage/emulated/0/Android/data/包名/file/Cache"
         *
         * @param context Context
         */
        public static void clearCacheDir(Context context) {
            final File cacheDir = context.getExternalCacheDir();
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    boolean b = deleteFile(cacheDir);
                    Log.i(TAG, "delete cache file " + b);
                }
            });
        }

        /**
         * 递归删除文件/目录
         * @param file File
         */
        private static boolean deleteFile(File file) {
            if (file == null || !file.exists()) {
                return true;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null) {
                    return true;
                }
                for (File f : files) {
                    deleteFile(f);
                }
            }
            return file.delete();
        }

    }
}
