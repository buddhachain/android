package com.chain.buddha.Xuper;

import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;


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


}
