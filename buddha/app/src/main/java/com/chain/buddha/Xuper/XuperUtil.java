package com.chain.buddha.Xuper;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.XuperClient;
import com.chain.buddha.retrofit.RetrofitUtil;

import java.math.BigInteger;


/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperUtil {

    private volatile static XuperUtil sInstance;
    private static XuperClient mClient;

    public static XuperUtil getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (sInstance == null) {
                    sInstance = new XuperUtil();
                }
            }
        }
        return sInstance;
    }

    private XuperUtil() {
        XuperClient client = new XuperClient("120.79.167.88:37101");
        Account account = Account.create("./keys");
        Account account1 = Account.create(1, 2);
        client.createContractAccount(account, "1111111111111111");
        client.transfer(account, "XC1111111111111111@xuper", BigInteger.valueOf(1000000), "1");
    }

    public static XuperClient getXuperClient() {
        if (mClient == null) {
            mClient = new XuperClient("120.79.167.88:37101");
        }
        return mClient;
    }

}
