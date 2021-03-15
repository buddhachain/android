package com.chain.buddha.utils;

import android.annotation.SuppressLint;

import com.bumptech.glide.request.RequestOptions;

/**
 * @Author: haroro
 * @CreateDate: 2/26/21
 */
public class GlideUtils {

    @SuppressLint("CheckResult")
    public static RequestOptions setImageHolderIcon(){
        RequestOptions options = new RequestOptions();
        //options.placeholder(R.drawable.ic_launcher_background); //设置加载未完成时的占位图
        //options.error(R.mipmap.icon_close_black); //设置加载异常时的占位图
        return options;
    }


}
