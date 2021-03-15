package com.chain.buddha.utils;

import android.app.Activity;

import com.chain.buddha.R;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class UIUtils {

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public static void finish(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        }
    }
}
