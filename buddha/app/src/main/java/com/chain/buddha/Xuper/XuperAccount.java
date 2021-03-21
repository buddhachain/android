package com.chain.buddha.Xuper;

import com.baidu.xuper.api.Account;


/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperAccount {

    private static Account mAccount;

    private XuperAccount() {

    }

    public static Account getAccount() {
        if (mAccount == null) {
            mAccount = Account.retrieve("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪", 1);
        }
        return mAccount;
    }

}
