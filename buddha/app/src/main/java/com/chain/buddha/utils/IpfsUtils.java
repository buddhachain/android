package com.chain.buddha.utils;

import android.content.Context;
import android.util.Log;

import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 文件上传下载工具类
 */
public class IpfsUtils {
    private static IPFS ipfs;
    public static String GET_IPFS_FILE_HEAD = "http://103.40.243.96:8080/ipfs/";

    public static IPFS getIpfs() {
        if (ipfs == null) {
            ipfs = new IPFS("/ip4/103.40.243.96/tcp/5001");
        }
        return ipfs;
    }

    /**
     * 上传文件
     *
     * @param filePath
     * @return
     */
    public static String uploadFile(String filePath) {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePath));
        try {
            MerkleNode addResult = getIpfs().add(file).get(0);
            Log.e("addResult", addResult.toString());
            return addResult.hash.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传文件列表
     *
     * @param filePathList
     * @return
     */
    public static List<String> uploadFileList(List<String> filePathList) {
        List<String> hashList = new ArrayList<>();
        for (String filePath : filePathList) {
            hashList.add(uploadFile(filePath));
        }
        return hashList;

    }

    /**
     * 文件下载
     *
     * @param key
     */
    public static byte[] getFile(String key) {
        Multihash filePointer = Multihash.fromBase58(key);
        try {
            byte[] fileContents = getIpfs().cat(filePointer);
            return fileContents;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
