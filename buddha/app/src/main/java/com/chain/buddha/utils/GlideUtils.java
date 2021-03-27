package com.chain.buddha.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chain.buddha.R;

import java.util.HashMap;

/**
 * @Author: haroro
 * @CreateDate: 2/26/21
 */
public class GlideUtils {

    static HashMap<String, byte[]> cacheImgByte = new HashMap<>();

    @SuppressLint("CheckResult")
    public static RequestOptions setImageHolderIcon() {
        RequestOptions options = new RequestOptions();
        //options.placeholder(R.drawable.ic_launcher_background); //设置加载未完成时的占位图
        //options.error(R.mipmap.icon_close_black); //设置加载异常时的占位图
        return options;
    }


    /**
     * 通过ipfskey 展示图片
     *
     * @param context
     * @param key
     * @param imageView
     */
    public static void loadImageByIpfskey(Context context, String key, ImageView imageView) {

        Glide.with(context)
                .load(IpfsUtils.GET_IPFS_FILE_HEAD + key)
                .placeholder(R.mipmap.ic_default_img)
                .dontAnimate()
                .into(imageView);
    }
}
