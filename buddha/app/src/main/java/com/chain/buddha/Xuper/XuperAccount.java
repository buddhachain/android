package com.chain.buddha.Xuper;

import android.app.Activity;
import android.content.Context;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.crypto.AES;
import com.baidu.xuper.crypto.Hash;
import com.chain.buddha.ui.activity.WalletGuideActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.FileUtils;
import com.chain.buddha.utils.SharePreferenceUtils;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


/**
 * 账户工具类
 *
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
    private static String mAccountAddress;//当前账户地址
    private static int mAccountType = ACCOUNT_TYPE_NULL;//当前账户类型

    public static final String VISITOR_ADDRESS = "eyY6Eez8z4Y3HvTWCM8r1VQDGBEbYkS6M";//游客改用私钥

    private XuperAccount() {

    }

    /**
     * 初始化账户
     */
    public static void initAccount() {
        if (SharePreferenceUtils.contains(SharePreferenceUtils.SP_USER_ADDRESS_KEY)) {
            mAccountAddress = SharePreferenceUtils.getString(SharePreferenceUtils.SP_USER_ADDRESS_KEY);
        }
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
     * 获取当前账户实体类(单例)
     *
     * @return
     */
    public static Account getTestAccount() {
        return Account.create();
    }

    /**
     * 复制当前账户实体类，并发送广播
     *
     * @param account
     */
    public static void setAccount(Account account) {
        setAccountType(account == null ? ACCOUNT_TYPE_NULL : ACCOUNT_TYPE_NORMAL);
        mAccount = account;
        if (mAccount != null) {
            if (!StringUtils.equals(mAccountAddress, mAccount.getAddress())) {
                SharePreferenceUtils.putString(SharePreferenceUtils.SP_USER_ADDRESS_KEY, mAccount.getAddress());
                mAccountAddress = mAccount.getAddress();
            }
        } else {
            mAccountAddress = null;
        }

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
        } else if (!StringUtils.equalsNull(mAccountAddress)) {
            return mAccountAddress;
        }
        return "";
    }

    /**
     * 验证密码
     *
     * @param mContext
     * @param psw
     * @return
     */
    public static boolean checkPsw(Context mContext, String psw) {
        try {
            Account account = getAccountFromFile(mContext, psw);
            if (account == null) {
                return false;
            }
            if (mAccount == null) {
                setAccount(account);
            }

        } catch (Throwable e) {
            return false;
        }
        return true;
    }


    /**
     * 是否有账号
     *
     * @return
     */
    public static boolean ifHasAccount(Context context) {
        try {
            String savepath = getAccountCachePath(context);
            boolean ifHas = new File(savepath, XuperConstants.KEY_SAVE_FILE_NAME).exists()
                    || SharePreferenceUtils.contains(SharePreferenceUtils.SP_USER_ADDRESS_KEY);
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
            SharePreferenceUtils.remove(SharePreferenceUtils.SP_USER_ADDRESS_KEY);
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
        checkAccount(activity, null);
    }

    /**
     * 账号检查
     *
     * @return
     */
    public static void checkAccount(Activity activity, DialogUtil.ConfirmCallBackObject<Boolean> callBack) {
        if (!ifHasAccount(activity)) {
            DialogUtil.simpleDialog(activity, "还没有账号，是否去导入", new DialogUtil.ConfirmCallBackObject<String>() {
                @Override
                public void onConfirmClick(String content) {
                    SkipInsideUtil.skipInsideActivity(activity, WalletGuideActivity.class);
                }
            }, null);

            if (callBack != null) {
                callBack.onConfirmClick(false);
            }
        } else if (!ifLoginAccount()) {
            DialogUtil.checkPswDialog(activity, "请输入密码，打开钱包", callBack, null);
        } else {
            if (callBack != null) {
                callBack.onConfirmClick(true);
            }
        }
    }

    /**
     * @param context
     * @param passwd  密码。
     */
    public static void saveAccount(Context context, Account account, String passwd) {
        if (account == null) {
            return;
        }
        setAccount(account);
        String path = getAccountCachePath(context);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        if (!path.endsWith("/")) {
            path += "/";
        }
        byte[] newPW = Hash.doubleSha256(passwd.getBytes());
        byte[] encryptContent = AES.encrypt(account.getMnemonic().getBytes(), newPW);
        writeFileUsingFileName(path + XuperConstants.KEY_SAVE_FILE_NAME, encryptContent);
    }

    private static void writeFileUsingFileName(String fileName, byte[] content) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encoded = encoder.encode(content);
        FileWriter writer;
        try {
            writer = new FileWriter(fileName);
            writer.write(encoded);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param passwd 密码。
     * @return Account 账户信息。
     */
    private static Account getAccountFromFile(Context context, String passwd) {
        try {
            String fileName = getAccountCachePath(context) + XuperConstants.KEY_SAVE_FILE_NAME;
//            byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileInputStream in = new FileInputStream(fileName);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                out.write(tempbytes, 0, byteread);
            }
            byte[] fileBytes = out.toByteArray();
            in.close();
            BASE64Decoder d = new BASE64Decoder();
            byte[] content = d.decodeBuffer(new String(fileBytes));
            byte[] newPasswd = Hash.doubleSha256(passwd.getBytes());
            byte[] p = AES.decrypt(content, newPasswd);
            String mnemonic = new String(p);
            int language = StringUtils.isChinese(mnemonic.charAt(0)) ? 1 : 2;
            return Account.retrieve(mnemonic, language);
        } catch (Throwable e) {
            return null;
        }

    }
}
