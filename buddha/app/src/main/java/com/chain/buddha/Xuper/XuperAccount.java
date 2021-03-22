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
            mAccount = TestAccount.getFounderAccount();
        }
        return mAccount;
    }


    public static String getAddress() {
        if (ifHasAccount()) {
            return getAccount().getAddress();
        }
        return "";
    }

    /**
     * 是否有账号
     *
     * @return
     */
    public static boolean ifHasAccount() {
        return mAccount != null;
    }

}
