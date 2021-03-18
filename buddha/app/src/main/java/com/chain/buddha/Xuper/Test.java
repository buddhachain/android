package com.chain.buddha.Xuper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Common;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
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
        if (!file1.exists()){
            file1.mkdirs();
        }
        if (!file1.exists()){
            file1.mkdirs();
        }
        savepath=savepath+"/testaccount.keys";
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

//            client.createContractAccount(account, testContractAccount);//成功
//            balance = client.getBalance(testContractAccount);//成功
//            client.transfer(account, "1111111111111355", BigInteger.valueOf(10), "1");//成功
//            balance = client.getBalance(testContractAccount);//成功
            client.createContractAccount(account, testContractAccount);//成功
            balance = client.getBalance(testContractAccount);//成功
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            client.transfer(account, "1111111111111355", BigInteger.valueOf(10), "1");//成功
            balance = client.getBalance(testContractAccount);//成功
//            XchainOuterClass.BCStatus status = client.getBlockchainStatus("xuper");
//            account.setContractAccount(testContractAccount);
//            Transaction transaction = client.queryContract(account, "wasm", "buddha", "is_master", new HashMap<>());//查询成功
//            String res = transaction.getContractResponse().getBodyStr();
//////            account.setContractAccount("XC1234567890200001@xuper");
//            transaction = client.queryContract(account, "wasm", "buddha", "list_kinddeed", new HashMap<>());//查询成功
//            res = transaction.getContractResponse().getBodyStr();
//            HashMap<String, byte[]> args = new HashMap<>();
//            args.put("kdid", ("1").getBytes());
//            transaction = client.queryContract(account, "wasm", "buddha", "list_kinddeeddetail", args);//查询成功
//            res = transaction.getContractResponse().getBodyStr();
            List<String> list = getBalanceDetails(account.getAddress());
            Log.d("xuper", "over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @param address account name, can be contract account
     * @return balance
     */
    public static List<String> getBalanceDetails(String address) {
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
    public static List<XchainOuterClass.ContractStatus> getAccountContracts(String account) {
        XchainOuterClass.GetAccountContractsRequest request = XchainOuterClass.GetAccountContractsRequest.newBuilder()
                .setHeader(Common.newHeader()).setAccount(account)
                .setBcname("xuper")
                .build();
        XchainOuterClass.GetAccountContractsResponse response = XchainGrpc.newBlockingStub(ManagedChannelBuilder.forTarget("120.79.167.88:37101")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build()).getAccountContracts(request);

        return response.getContractsStatusList();
    }
}
