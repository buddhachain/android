package com.chain.buddha.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: haroro
 * @CreateDate: 2/26/21
 */
public class GlideUtils {

    @SuppressLint("CheckResult")
    public static RequestOptions setImageHolderIcon() {
        RequestOptions options = new RequestOptions();
        //options.placeholder(R.drawable.ic_launcher_background); //设置加载未完成时的占位图
        //options.error(R.mipmap.icon_close_black); //设置加载异常时的占位图
        return options;
    }


    public static void loadByteImage(Context context, byte[] bytes, ImageView imageView) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapUtils.getBitmapFromBytes(bytes);
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bitmapBytes = baos.toByteArray();
        Glide.with(context)
                .load(bytes)
                .placeholder(R.mipmap.ic_default_img)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 通过ipfskey 展示图片
     *
     * @param context
     * @param key
     * @param imageView
     */
    public static void loadImageByIpfskey(Context context, String key, ImageView imageView) {

        Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                try {
                    byte[] bytes = IpfsUtils.getFile(key);
                    emitter.onNext(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        loadByteImage(context, bytes, imageView);
                    }

                    @Override
                    public void onFail(String message) {
                    }
                }));
    }
}
