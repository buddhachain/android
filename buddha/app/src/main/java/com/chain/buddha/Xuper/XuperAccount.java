package com.chain.buddha.Xuper;

import android.app.Activity;

import com.baidu.xuper.api.Account;
import com.chain.buddha.BuddhaApplication;
import com.chain.buddha.ui.activity.LoginActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.FileUtils;
import com.chain.buddha.utils.SkipInsideUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperAccount {

    public static final int ACCOUNT_TYPE_NULL = -1;
    public static final int ACCOUNT_TYPE_NORMAL = 0;
    public static final int ACCOUNT_TYPE_FASHI = 1;
    public static final int ACCOUNT_TYPE_SIYUAN = 2;
    public static final int ACCOUNT_TYPE_JJH = 3;

    private static Account mAccount;
    private static int mAccountType = -1;

    private XuperAccount() {

    }

    public static Account getAccount() {
//        if (mAccount == null) {
//            mAccount = TestAccount.getFounderAccount();
//        }
        return mAccount;
    }

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
    public static boolean ifHasAccount() {
        try {
            String savepath = FileUtils.SDCardConstants.getDir(BuddhaApplication.getInstance()) + XuperConstants.KEY_SAVE_FILE_NAME;
            return new File(savepath).exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取账户缓存路径
     *
     * @return
     */
    public static String getAccountCachePath() {
        return FileUtils.SDCardConstants.getDir(BuddhaApplication.getInstance()) + XuperConstants.KEY_SAVE_FILE_NAME;
    }

    /**
     * 删除账号缓存
     *
     * @return
     */
    public static boolean logoutAccount() {
        try {
            setAccount(null);
            String savepath = FileUtils.SDCardConstants.getDir(BuddhaApplication.getInstance()) + XuperConstants.KEY_SAVE_FILE_NAME;
            return new File(savepath).delete();
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
        if (!ifHasAccount()) {
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
