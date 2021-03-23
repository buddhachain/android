package com.chain.buddha.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.chain.buddha.ui.BaseActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by haroro on 2019/9/19.
 */

public class SkipInsideUtil {

    public final static int DATA_ACTIVITY_CODE_OK = 1; //修改用户信息成功
    public final static int DATA_ACTIVITY_CODE_CANCLE = 2;  //修改用户信息失败
    public final static int DATA_ACTIVITY_CODE = 3;  //修改用户信息失败

    public final static String SKIP_KEY_MNEMONIC = "mnemonic";  //助记词
    public final static String SKIP_KEY_KDID = "kdid";  //善举id
    public final static String SKIP_KEY_NUM = "num";  //助记词
    public final static String SKIP_KEY_SPEC = "spec";  //善举id

    /**
     * 没有参数的内部跳转，不需返回值
     */
    public static void skipInsideActivity(Context activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void skipInsideActivity(Context activity, Class<? extends Activity> cls, String key, Serializable value) {
        Intent intent = new Intent(activity, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, value);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * 带参数的内部跳转，不需要返回值
     */
    public static void skipInsideActivity(Context activity, Class<? extends Activity> cls, HashMap<String, ? extends Object> hashMap) {
        Intent intent = new Intent(activity, cls);
        Iterator<? extends Object> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            Object value = entry.getValue();
            String key = entry.getKey();

            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            }
            if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            }
            if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            }
            if (value instanceof Float) {
                intent.putExtra(key, (float) value);
            }
            if (value instanceof Double) {
                intent.putExtra(key, (double) value);

            }
        }
        activity.startActivity(intent);

    }

    public static void skipInsideActivityForResult(Context activity, Class<? extends Activity> cls, int resultCode) {
        Intent intent = new Intent(activity, cls);

        ((BaseActivity) activity).startActivityForResult(intent, resultCode);
    }

    public static void skipInsideActivityForResult(Context activity, Class<? extends Activity> cls, int resultCode, String key, String value) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, value);
        ((BaseActivity) activity).startActivityForResult(intent, resultCode);
    }

}
