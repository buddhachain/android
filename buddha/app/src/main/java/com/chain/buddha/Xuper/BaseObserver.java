package com.chain.buddha.Xuper;

import android.util.Log;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by Spencer on 5/2/18.
 */
public class BaseObserver<T> implements Observer<T> {

    private boolean hasDialog;
    private ResponseCallBack<T> responseCallBack;
    public Type mType;

    public BaseObserver(boolean hasDialog, ResponseCallBack<T> responseCallBack) {
        this.hasDialog = hasDialog;
        this.responseCallBack = responseCallBack;
        mType = getSuperclassTypeParameter(this.getClass());
    }


    @Override
    public void onSubscribe(Disposable d) {

        if (hasDialog) {
            //TODO 加载动画
        }
        //repeatFunction.setStop(false);

    }

    @Override
    public void onNext(T tXuperResponse) {
        responseCallBack.onSuccess(tXuperResponse);
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof SocketTimeoutException) {//请求超时
                responseCallBack.onFail("请求超时,请稍后再试");
            } else if (e instanceof ConnectException) {//网络连接超时
                responseCallBack.onFail("网络连接超时,请检查网络状态");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                responseCallBack.onFail("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    responseCallBack.onFail("网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    responseCallBack.onFail("请求的地址不存在");
                } else {
                    responseCallBack.onFail("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                responseCallBack.onFail("域名解析失败");
            } else {
                responseCallBack.onFail("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
//            if (disposable != null && !disposable.isDisposed()) { //事件完成取消订阅
//                disposable.dispose();
//            }
            //Todo dialog
        }


    }

    @Override
    public void onComplete() {
        if (hasDialog) {
            //TODO 加载动画
        }
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

}
