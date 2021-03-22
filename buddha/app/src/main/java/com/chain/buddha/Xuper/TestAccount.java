package com.chain.buddha.Xuper;

import com.baidu.xuper.api.Account;

import java.math.BigInteger;

public class TestAccount {

    /**
     * 测试账户 只后悔删除
     */

    private static Account mFounderAccount, mFashiAccount, mSimpleAccount1, mSimpleAccount2;

    private TestAccount() {

    }

    public static Account getFashiAccount() {
        if (mFashiAccount == null) {
            mFashiAccount = Account.retrieve("once better praise warm spoon misery tiny home goose scare mercy can", 2);
        }
        return mFashiAccount;
    }


    public static Account getFounderAccount() {
        if (mFounderAccount == null) {
            mFounderAccount = Account.retrieve("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪", 1);
        }
        return mFounderAccount;
    }


    public static Account getSimpleAccount1() {
        if (mSimpleAccount1 == null) {
            mSimpleAccount1 = Account.retrieve("hazard globe drive supply divorce canvas toilet fault tomato crater potato calm", 2);//成功
        }
        return mSimpleAccount1;
    }

    public static void transferTo(String address, int value) {
        XuperApi.getXuperClient().transfer(getFounderAccount(), address, BigInteger.valueOf(value), "1");//成功
    }
}
