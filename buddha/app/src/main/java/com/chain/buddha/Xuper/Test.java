package com.chain.buddha.Xuper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Common;
import com.baidu.xuper.api.Proposal;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.baidu.xuper.config.Config;
import com.baidu.xuper.pb.XchainGrpc;
import com.baidu.xuper.pb.XchainOuterClass;
import com.chain.buddha.utils.FileUtils;
import com.chain.buddha.utils.ThreadUtils;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.ManagedChannelBuilder;

public class Test {
    private static XuperClient client;

    @SuppressLint("SdCardPath")
    private static final String SD_PATH = "/sdcard/buddha/";
    private static final String IN_PATH = "/buddha/";

    public static void test(Context context) {

        //账号SDK测试
        client = new XuperClient("120.79.167.88:37101");
        String psw = "123456";
//        Account account = Account.create("./keys");
        // String savepath = FileUtils.SDCardConstants.getCacheDir(context) + "/testaccount.keys";
        String savepath;
        Account account;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savepath = SD_PATH;
        } else {
            savepath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        File file1 = new File(savepath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
        savepath = savepath + "/testaccount.keys";
        if (new File(savepath).exists()) {
            account = Account.getAccountFromFile(savepath, psw);//成功
        } else {
            account = Account.createAndSave(savepath, psw, 1, 2);//成功
        }
//        account = Account.create();
        account = Account.retrieve("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪", 1);
        try {
            String testContractAccount = "1111111111111301";
            BigInteger balance = client.getBalance(account.getAddress());//成功
            XuperClient.BalDetails[] balDetails = client.getBalanceDetails(account.getAddress());//成功
//            account.setContractAccount(testContractAccount);

            client.createContractAccount(account, testContractAccount);//成功
//            balance = client.getBalance(testContractAccount);//成功
//            client.transfer(account, "1111111111111355", BigInteger.valueOf(10), "1");//成功
//            balance = client.getBalance(testContractAccount);//成功
//            client.createContractAccount(account, testContractAccount);//成功
            balance = client.getBalance(testContractAccount);//成功
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {

                }
            });
//            client.transfer(account, "1111111111111355", BigInteger.valueOf(10), "1");//成功
//            balance = client.getBalance(testContractAccount);//成功
//            XchainOuterClass.BCStatus status = client.getBlockchainStatus("xuper");
//            account.setContractAccount(testContractAccount);
//            Transaction transaction = client.invokeContract(account, "wasm", "buddha", "is_master", new HashMap<>());//查询成功
//            String res = transaction.getContractResponse().getBodyStr();
//////            account.setContractAccount("XC1234567890200001@xuper");
//            transaction = client.queryContract(account, "wasm", "buddha", "list_kinddeed", new HashMap<>());//查询成功
//            res = transaction.getContractResponse().getBodyStr();
//            HashMap<String, byte[]> args = new HashMap<>();
//            args.put("kdid", ("1").getBytes());
//            transaction = client.queryContract(account, "wasm", "buddha", "list_kinddeeddetail", args);//查询成功
//            res = transaction.getContractResponse().getBodyStr();
            List<String> list = getAccountByAK(account.getAddress());
            Log.d("xuper", "over");

            mulSignContract();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test2(Context context) {
//账号SDK测试
        client = new XuperClient("120.79.167.88:37101");
        mulSignContract();
    }

    public static void transferTo(String address, int value) {

        Account account = Account.retrieve("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪", 1);
        client.transfer(account, address, BigInteger.valueOf(value), "1");//成功

    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @param address account name, can be contract account
     * @return balance
     */
    public static List<String> getAccountByAK(String address) {
        XchainOuterClass.AK2AccountRequest request = XchainOuterClass.AK2AccountRequest.newBuilder()
                .setHeader(Common.newHeader())
                .setAddress(address)
                .setBcname("xuper")
                .build();
        XchainOuterClass.AK2AccountResponse response = XchainGrpc.newBlockingStub(ManagedChannelBuilder.forTarget("120.79.167.88:37101")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build()).getAccountByAK(request);

        return response.getAccountList();
    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @param account account name, can be contract account
     * @return balance
     */
    public static XchainOuterClass.Acl getAccountContractsList(String account) {
        XchainOuterClass.AclStatus request = XchainOuterClass.AclStatus.newBuilder()
                .setHeader(Common.newHeader())
                .setAccountName(account)
                .setBcname("xuper")
                .build();
        XchainOuterClass.AclStatus response = XchainGrpc.newBlockingStub(ManagedChannelBuilder.forTarget("120.79.167.88:37101")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build()).queryACL(request);

        return response.getAcl();
    }

    //多签测试  成功
    public static void mulCreateACL() {
        Account account1 = Account.retrieve("hazard globe drive supply divorce canvas toilet fault tomato crater potato calm", 2);//成功
        Account account2 = Account.retrieve("once better praise warm spoon misery tiny home goose scare mercy can", 2);//成功
        transferTo(account1.getAKAddress(), 100);
        transferTo(account2.getAKAddress(), 100);
        BigInteger balance = client.getBalance(account1.getAddress());//成功
        balance = client.getBalance(account2.getAddress());//成功
        Account account = Account.retrieve("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪", 1);
        String accountName = "1234567890500013";
        //创建账号
        String desc = "{\"aksWeight\": {\"" + account1.getAddress() + "\": 0.3,\"" + account2.getAddress() + "\":0.3}, \"pm\": {\"acceptValue\": 1.0, \"rule\": 1}}";
        Map<String, byte[]> args = new HashMap<>();
        args.put("account_name", accountName.getBytes());
        args.put("acl", desc.getBytes());
        Transaction transaction = client.invokeContract(account, "xkernel", "", "NewAccount", args);
        String res = transaction.getContractResponse().getBodyStr();
        Log.d("xuper", "over");
        getAccountContractsList("XC" + accountName + "@xuper");

    }

    //多签测试  成功

    /**
     * 1234567890500013是多签账号 由两个account控制
     * 1234567890500023 只由account1控制
     */
    public static void mulSignContract() {
        try {

            String newAccountName = "XC1234567890500012@xuper";
            String contractAccountName = "XC1234567890500013@xuper";
            Account account1 = Account.retrieve("hazard globe drive supply divorce canvas toilet fault tomato crater potato calm", 2);//成功
            Account account2 = Account.retrieve("once better praise warm spoon misery tiny home goose scare mercy can", 2);//成功
//            transferTo(account1.getAKAddress(), 1000);
//        transferTo(account2.getAKAddress());
//            account1=new Account()
//            getAccountByAK(account1.getAKAddress());
//            getAccountContractsList(newAccountName);
            account1.setContractAccount(newAccountName);
//            client.createContractAccount(account1, newAccountName);//成功
//            client.transfer(account1, account2.getAKAddress(), BigInteger.valueOf(100), "1");//成功
            BigInteger balance = client.getBalance(account1.getAddress());//成功
            balance = client.getBalance(account2.getAddress());//成功
            balance = client.getBalance(contractAccountName);//成功

            Map<String, byte[]> args = new HashMap<>();
            args.put("account_name", newAccountName.getBytes());
            args.put("account", newAccountName.getBytes());
//            args.put("acl", desc.getBytes());
            Transaction transaction;
            String res;

            transaction = client.queryContract(account1, "wasm", "buddha", "is_master", args);//查询成功
            res = transaction.getContractResponse().getBodyStr();
            transaction = client.invokeContract(account1, "wasm", "buddha", "is_master", args);//查询成功
            res = transaction.getContractResponse().getBodyStr();
            Log.d("xuper", "over");
            getAccountByAK(account1.getAKAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
