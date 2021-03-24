package com.chain.buddha.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.xuper.api.Transaction;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IpfsUtils {
    private static IPFS ipfs;

    public static IPFS getIpfs() {
        if (ipfs == null) {
            ipfs = new IPFS("/ip4/103.40.243.96/tcp/5001");
//            ipfs = new IPFS("103.40.243.96", 37101);

//输入整个地址
//            MultiAddress multiaddr = new MultiAddress("http://103.40.243.96:4001");
//            ipfs = new IPFS(multiaddr);
            try {
//                ipfs.refs.local();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ipfs;
    }

    public static String uploadFile(String filePath) {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePath));
        try {
            MerkleNode addResult = getIpfs().add(file).get(0);
            Log.e("addResult", addResult.toString());
            return addResult.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void getFile(String key) {
        Multihash filePointer = Multihash.fromBase58(key);
        try {
            byte[] fileContents = getIpfs().cat(filePointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试上传文件(成功)
     *
     * @param context
     */
    public static void test(Context context) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    String savepath = FileUtils.SDCardConstants.getCacheDir(context) + "/hello.txt";
                    uploadFile(savepath);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String o) {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                }));
    }
}
