package com.chain.buddha.Xuper;

import android.app.Activity;
import android.content.Context;

import com.baidu.xuper.api.Account;
import com.chain.buddha.ui.activity.LoginActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.FileUtils;
import com.chain.buddha.utils.SkipInsideUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * 账户工具类
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperAccount {

    public static final int ACCOUNT_TYPE_NULL = -1;//账户类型 无账号
    public static final int ACCOUNT_TYPE_NORMAL = 0;//账户类型 普通账号
    public static final int ACCOUNT_TYPE_FASHI = 1;//账户类型 法师
    public static final int ACCOUNT_TYPE_SIYUAN = 2;//账户类型 寺院
    public static final int ACCOUNT_TYPE_JJH = 3;//账户类型 基金会

    private static Account mAccount;//当前账户实体类
    private static int mAccountType = -1;//当前账户类型

    private XuperAccount() {

    }

    /**
     * 获取当前账户实体类(单例)
     *
     * @return
     */
    public static Account getAccount() {
        return mAccount;
    }

    /**
     * 复制当前账户实体类，并发送广播
     *
     * @param account
     */
    public static void setAccount(Account account) {
        setAccountType(account == null ? ACCOUNT_TYPE_NULL : ACCOUNT_TYPE_NORMAL);
        mAccount = account;
        EventBus.getDefault().post(new EventBeans.LoginEvent());
    }

    public static int getAccountType() {
        return mAccountType;
    }

    public static void setAccountType(int mAccountType) {
        XuperAccount.mAccountType = mAccountType;
    }

    /**
     * 获取账户地址
     *
     * @return
     */
    public static String getAddress() {
        if (ifLoginAccount()) {
            return getAccount().getAddress();
        }
        return "";
    }

    /**
     * 是否有账号
     *
     * @return
     */
    public static boolean ifHasAccount(Context context) {
        try {
            String savepath = getAccountCachePath(context);
            boolean ifHas = new File(savepath, XuperConstants.KEY_SAVE_FILE_NAME).exists();
            return ifHas;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取账户缓存路径
     *
     * @return
     */
    public static String getAccountCachePath(Context context) {
        String cacheFile = FileUtils.SDCardConstants.getDir(context) + XuperConstants.KEY_SAVE_FILE_PATH;
        return cacheFile;
    }

    /**
     * 删除账号缓存
     *
     * @return
     */
    public static boolean logoutAccount(Context context) {
        try {
            String savepath = getAccountCachePath(context);
            boolean b = new File(savepath, XuperConstants.KEY_SAVE_FILE_NAME).delete();
            setAccount(null);
            return b;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean ifLoginAccount() {
        return mAccount != null;
    }

    /**
     * 账号检查
     *
     * @return
     */
    public static void checkAccount(Activity activity) {
        if (!ifHasAccount(activity)) {
            DialogUtil.simpleDialog(activity, "还没有账号，是否去导入", new DialogUtil.ConfirmCallBackInf() {
                @Override
                public void onConfirmClick(String content) {
                    SkipInsideUtil.skipInsideActivity(activity, LoginActivity.class);
                }
            }, null);
        } else if (!ifLoginAccount()) {
            DialogUtil.checkPswDialog(activity, "请输入密码，打开钱包", null, null);
        }
    }
}
