package com.chain.buddha.Xuper;

import android.util.Log;

import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.chain.buddha.bean.ShanjvBean;
import com.chain.buddha.bean.XuperResponse;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.HttpException;


/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperApi {

    private static XuperClient mClient;

    public static XuperClient getXuperClient() {
        if (mClient == null) {
            mClient = new XuperClient("120.79.167.88:37101");
        }
        return mClient;
    }

    public static void getQifulist() {
        HashMap<String, byte[]> hashMap = new HashMap<>();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Transaction transaction = mClient.invokeContract(XuperAccount.getAccount(), "wasm", "buddha", "list_kinddeed", hashMap);
                String bodyStr = transaction.getContractResponse().getBodyStr();
                Gson gson = new Gson();
                emitter.onNext(bodyStr);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(false, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String transaction) {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                }));


    }

    public static void test() {
        baseRequest("list_kinddeed", new HashMap<>(), new BaseObserver<XuperResponse<ShanjvBean>>(false, new ResponseCallBack<XuperResponse<ShanjvBean>>() {
            @Override
            public void onSuccess(XuperResponse<ShanjvBean> o) {
                Log.d("shanjv", o.getMessage().getId());
            }

            @Override
            public void onFail(String message) {

            }
        }));

    }

    private static void baseRequest(String method, HashMap<String, byte[]> hashMap, BaseObserver baseObserver) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                Transaction transaction = mClient.invokeContract(XuperAccount.getAccount(), "wasm", "buddha", method, hashMap);
                String bodyStr = transaction.getContractResponse().getBodyStr();
                bodyStr = "{\"id\" : \"1\"}";
                Gson gson = new Gson();
                try {

                    Object object = gson.fromJson(bodyStr, baseObserver.mType);
                    emitter.onNext(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);

    }


}
