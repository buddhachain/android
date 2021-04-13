package com.chain.buddha.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.chain.buddha.BuddhaApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SharePreferenceUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String SAVE_FILE_NAME_DEFAULT = "share_data";
    public static String SAVE_FILE_NAME = SAVE_FILE_NAME_DEFAULT;

    private static final int SAVE_MODE = Context.MODE_PRIVATE;

    public static final String SP_USER_ADDRESS_KEY = "sp_user_address_key";

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME, SAVE_MODE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME, SAVE_MODE);
        return sharedPreferences.getString(key, "");
    }

    public static Boolean contains(String key) {
        SharedPreferences sharedPreferences = BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME, SAVE_MODE);
        return sharedPreferences.contains(key);
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME, SAVE_MODE);
        sharedPreferences.edit().remove(key).commit();
    }

    public static void clear() {
        SharedPreferences sharedPreferences = BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME, SAVE_MODE);
        sharedPreferences.edit().clear().commit();
    }


}
