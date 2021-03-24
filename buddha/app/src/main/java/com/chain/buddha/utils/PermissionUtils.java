package com.chain.buddha.utils;

import android.Manifest;
import com.chain.buddha.R;

/**
 * 检查权限/权限数组
 * request权限
 */
public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getName();

    public static final String[] PERMISSION_MANIFEST = {
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static final String[] PERMISSION_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 无权限时对应的提示内容
     */
    public static final int[] NO_PERMISSION_TIP = {
            R.string.app_name,
    };

}
