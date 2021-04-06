package com.chain.buddha.Xuper;

import android.util.Log;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.Common;
import com.baidu.xuper.api.ContractResponse;
import com.baidu.xuper.api.Proposal;
import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.baidu.xuper.config.Config;
import com.baidu.xuper.crypto.ECKeyPair;
import com.baidu.xuper.crypto.Hash;
import com.baidu.xuper.pb.XchainGrpc;
import com.baidu.xuper.pb.XchainOuterClass;
import com.chain.buddha.BuddhaApplication;
import com.chain.buddha.utils.DialogUtil;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.grpc.ManagedChannelBuilder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 请求接口API
 *
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperApi {

    private static XuperClient mClient;
    public static final String METHOD_LIST_KINDDEED = "list_kinddeed";

    public static XuperClient getXuperClient() {
        if (mClient == null) {
            mClient = new XuperClient("120.79.167.88:37101");
        }
        return mClient;
    }


    /**
     * 请求善举列表
     *
     * @param responseCallBack
     */
    public static void requestQifuList(ResponseCallBack responseCallBack) {
        baseQueryRequest(XuperApi.METHOD_LIST_KINDDEED, new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 是否基金会
     *
     * @param responseCallBack
     */
    public static void getIsFounder(ResponseCallBack<String> responseCallBack) {
        baseQueryRequest("is_founder", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 是否法师
     *
     * @param responseCallBack
     */
    public static void getIsMaster(ResponseCallBack<String> responseCallBack) {
        baseQueryRequest("is_master", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 是否寺院
     *
     * @param responseCallBack
     */
    public static void getIsTemple(ResponseCallBack<String> responseCallBack) {
        baseQueryRequest("is_temple", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 获取余额
     *
     * @param address
     */
    public static void getBalance(String address, ResponseCallBack<String> responseCallBack) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    String balance = getXuperClient().getBalance(address).toString();//成功String bodyStr = transaction.getContractResponse().getBodyStr();
                    emitter.onNext(balance);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));
    }

    /**
     * 获取余额
     *
     * @param address
     */
    public static void getBalanceDetails(String address, ResponseCallBack<XuperClient.BalDetails[]> responseCallBack) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    XuperClient.BalDetails[] balanceList = getXuperClient().getBalanceDetails(address);//成功String bodyStr = transaction.getContractResponse().getBodyStr();
                    emitter.onNext(balanceList);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));
    }

    /**
     * 申请成为法师
     *
     * @param responseCallBack
     */
    public static void applyMaster(String creditcode, String proof, ResponseCallBack<String> responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("creditcode", creditcode.getBytes());
        args.put("proof", proof.getBytes());

        baseInvokeRequest("apply_master", args, new BaseObserver(false, responseCallBack));
    }


    /**
     * 普通合约请求
     *
     * @param method
     * @param hashMap
     * @param baseObserver
     */
    private static void baseInvokeRequest(String method, HashMap<String, byte[]> hashMap, BaseObserver baseObserver) {
        if (!XuperAccount.ifLoginAccount()) {
            baseObserver.onError(new Exception("还未登陆"));
            return;
        }
        Observable.create(new ObservableOnSubscribe<Transaction>() {
            @Override
            public void subscribe(ObservableEmitter<Transaction> emitter) throws Exception {
                try {
                    Transaction transaction;

                    Proposal p = new Proposal();
                    if (Config.getInstance().getComplianceCheck().getIsNeedComplianceCheck()) {
                        p.addAuthRequire(Config.getInstance().getComplianceCheck().getComplianceCheckEndorseServiceAddr());
                    }
                    p.setInitiator(XuperAccount.getAccount());
                    transaction = p.invokeContract("wasm", "buddha", method, hashMap).build(getXuperClient());

                    emitter.onNext(transaction);

                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<Transaction>() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        DialogUtil.simpleDialog(BuddhaApplication.getCurrencyActivity(), "手续费" + transaction.getGasUsed(), new DialogUtil.ConfirmCallBackInf() {
                            @Override
                            public void onConfirmClick(String content) {
                                sendRequest(transaction, baseObserver);
                            }
                        });

                    }

                    @Override
                    public void onFail(String message) {
                        baseObserver.onError(new Throwable(message));
                    }
                }));

    }

    /**
     * 发送合约请求
     *
     * @param transaction
     * @param baseObserver
     */
    private static void sendRequest(Transaction transaction, BaseObserver baseObserver) {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {

                    ContractResponse response = transaction.sign().send(getXuperClient()).getContractResponse();

                    String bodyStr = response.getBodyStr();

                    if (baseObserver.mType == String.class) {
                        emitter.onNext(bodyStr);
                    } else {
                        Gson gson = new Gson();
                        Object object = gson.fromJson(bodyStr, baseObserver.mType);
                        emitter.onNext(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);

    }

    /**
     * 普通查询合约请求
     *
     * @param method
     * @param hashMap
     * @param baseObserver
     */
    private static void baseQueryRequest(String method, HashMap<String, byte[]> hashMap, BaseObserver baseObserver) {
        if (!XuperAccount.ifLoginAccount()) {
            baseObserver.onError(new Exception("还未登陆"));
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    ContractResponse response = getXuperClient().queryContract(XuperAccount.getAccount(), "wasm", "buddha", method, hashMap).getContractResponse();
                    String bodyStr = response.getBodyStr();

                    if (baseObserver.mType == String.class) {
                        emitter.onNext(bodyStr);
                    } else {
                        Gson gson = new Gson();
                        Object object = gson.fromJson(bodyStr, baseObserver.mType);
                        emitter.onNext(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);

    }

    /**
     * 普通合约请求(无需验证用户)
     *
     * @param method
     * @param hashMap
     * @param baseObserver
     */
    private static void baseQueryRequest2(String method, HashMap<String, byte[]> hashMap, BaseObserver baseObserver) {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    String bodyStr;

                    XchainOuterClass.ContractResponse resp = queryContract("wasm", "buddha", method, hashMap);
                    Log.e("resp", resp.getMessage());
                    bodyStr = new String(resp.getBody().toByteArray());

                    if (baseObserver.mType == String.class) {
                        emitter.onNext(bodyStr);
                    } else {
                        Gson gson = new Gson();
                        Object object = gson.fromJson(bodyStr, baseObserver.mType);
                        emitter.onNext(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);

    }

    /**
     * 请求合约(不需要验证用户)
     *
     * @param module   module of contract, usually wasm
     * @param contract contract name
     * @param method   contract method
     * @param args     contract method arguments
     * @return
     */
    private static XchainOuterClass.ContractResponse queryContract(String module, String contract, String method, Map<String, byte[]> args) {
        try {

            Map<String, ByteString> newArgs = new HashMap<>();
            for (Map.Entry<String, byte[]> entry : args.entrySet()) {
                newArgs.put(entry.getKey(), ByteString.copyFrom(entry.getValue()));
            }
            ArrayList<XchainOuterClass.InvokeRequest> requests = new ArrayList<>();
            requests.add(XchainOuterClass.InvokeRequest.newBuilder()
                    .setModuleName(module)
                    .setMethodName(method)
                    .setContractName(contract)
                    .putAllArgs(newArgs)
                    .build());
            XchainOuterClass.InvokeRPCRequest request = XchainOuterClass.InvokeRPCRequest.newBuilder()
                    .setBcname("xuper")
                    .addAllRequests(requests)
                    .setInitiator("eyY6Eez8z4Y3HvTWCM8r1VQDGBEbYkS6M")
                    .build();
            XchainOuterClass.InvokeRPCResponse invokeRPCResponse = XchainGrpc.newBlockingStub(ManagedChannelBuilder.forTarget("120.79.167.88:37101").usePlaintext()
                    .build()).preExec(request);
            XchainOuterClass.ContractResponse resp = invokeRPCResponse.getResponse().getResponses(invokeRPCResponse.getResponse().getResponseCount() - 1);
            Log.e("resp", resp.getMessage());
            return resp;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 带转账的合约调用
     *
     * @param method
     * @param hashMap
     * @param amount
     * @param baseObserver
     */
    private static void baseRequestWithAmount(String method, HashMap<String, byte[]> hashMap, String amount, BaseObserver baseObserver) {
        if (!XuperAccount.ifLoginAccount()) {
            baseObserver.onError(new Exception("还未登陆"));
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    Transaction transaction;
                    transaction = invokeContractWithAmount(XuperAccount.getAccount(), "wasm", "buddha", method, amount, hashMap);

                    String bodyStr = transaction.getContractResponse().getBodyStr();
                    if (baseObserver.mType == String.class) {
                        emitter.onNext(bodyStr);
                    } else {
                        Gson gson = new Gson();
                        Object object = gson.fromJson(bodyStr, baseObserver.mType);
                        emitter.onNext(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);

    }

    /**
     * @param from     the initiator of calling method
     * @param module   module of contract, usually wasm
     * @param contract contract name
     * @param method   contract method
     * @param args     contract method arguments
     * @return
     */
    public static Transaction invokeContractWithAmount(Account from, String module, String contract, String method, String amount, Map<String, byte[]> args) {
        Proposal p = new Proposal();
        if (Config.getInstance().getComplianceCheck().getIsNeedComplianceCheck()) {
            p.addAuthRequire(Config.getInstance().getComplianceCheck().getComplianceCheckEndorseServiceAddr());
        }
        p.setInitiator(from);
        return p.transfer(contract, new BigInteger(amount)).invokeContract(module, contract, method, args).build(getXuperClient()).sign().send(getXuperClient());
    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @param account account name, can be contract account
     * @return balance
     */
    public static void getAccountContractsList(String account, ResponseCallBack<XchainOuterClass.Acl> responseCallBack) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
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

                    emitter.onNext(response.getAcl());
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));
    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @return balance
     */
    public static void getUTXO(ResponseCallBack<XchainOuterClass.Acl> responseCallBack) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    Account initiator = XuperAccount.getAccount();
                    byte[] hash = Hash.doubleSha256(("xuper" + initiator.getAKAddress() + "5" + false).getBytes());
                    byte[] sign = initiator.getKeyPair().sign(hash);

                    XchainOuterClass.SignatureInfo siginfo = XchainOuterClass.SignatureInfo.newBuilder()
                            .setPublicKey(XuperAccount.getAccount().getKeyPair().getJSONPublicKey())
                            .setSign(ByteString.copyFrom(sign))
                            .build();
                    XchainOuterClass.UtxoInput request = XchainOuterClass.UtxoInput.newBuilder()
                            .setHeader(Common.newHeader())
                            .setAddress(XuperAccount.getAddress())
                            .setTotalNeed("5")
                            .setPublickey(XuperAccount.getAccount().getKeyPair().getJSONPublicKey())
                            .setBcname("xuper")
                            .setUserSign(siginfo.getSign())
                            .build();
                    XchainOuterClass.UtxoOutput response = XchainGrpc.newBlockingStub(ManagedChannelBuilder.forTarget("120.79.167.88:37101")
                            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                            // needing certificates.
                            .usePlaintext()
                            .build()).selectUTXO(request);

                    emitter.onNext(response.getUtxoListList());
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));
    }

    /**
     * Get balance unfrozen balance and frozen balance of account
     *
     * @param address account name, can be contract account
     *                根据本地账号获取合约账号
     * @return balance
     */
    public static void getAccountByAK(String address, ResponseCallBack<List<String>> responseCallBack) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {

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
                    emitter.onNext(response.getAccountList());
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));
    }


    public static void transferTo(String address, String value, ResponseCallBack<String> responseCallBack) {
        if (!XuperAccount.ifLoginAccount()) {
            responseCallBack.onFail("还未登陆");
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    Transaction transaction = getXuperClient().transfer(XuperAccount.getAccount(), address, new BigInteger(value), "0");
                    String txId = transaction.getTxid();
//                    String bodyStr = transaction.getContractResponse().getBodyStr();
                    emitter.onNext(txId);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(false, responseCallBack));

    }


    /**
     * 请求法师列表
     *
     * @param responseCallBack
     */
    public static void requestMasterList(ResponseCallBack responseCallBack) {
        baseQueryRequest("list_master", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求寺院列表
     *
     * @param responseCallBack
     */
    public static void requestTempleList(ResponseCallBack responseCallBack) {
        baseQueryRequest("list_temple", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求善举凭证列表
     *
     * @param responseCallBack
     */
    public static void requestKinddeedproofList(ResponseCallBack responseCallBack) {
        baseQueryRequest("list_kinddeedproof", new HashMap<>(), new BaseObserver(false, responseCallBack));
    }


    /**
     * 上传善举凭证
     *
     * @param responseCallBack
     */
    public static void uploadKinddeedproof(String orderid, String proof, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("orderid", orderid.getBytes());
        args.put("proof", proof.getBytes());
        args.put("timestamp", (System.currentTimeMillis() + "").getBytes());

        baseInvokeRequest("upload_kinddeedproof", args, new BaseObserver(false, responseCallBack));

    }

    /**
     * 请求善举详情
     *
     * @param responseCallBack
     */
    public static void kinddeedDetail(String kdid, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("kdid", kdid.getBytes());

        baseQueryRequest("list_kinddeeddetail", args, new BaseObserver(false, responseCallBack));
    }


    /**
     * 请求善举商品列表
     *
     * @param responseCallBack
     */
    public static void kinddeedSpec(String kdid, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("kdid", kdid.getBytes());

        baseQueryRequest("list_kinddeedspec", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 购买善举
     *
     * @param responseCallBack
     */
    public static void prayKinddeed(String spec, String count, String kdid, String amount, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", (new Random().nextInt(1000) + "").getBytes());
        args.put("kinddeed", kdid.getBytes());
        args.put("spec", spec.getBytes());
        args.put("count", count.getBytes());
        args.put("timestamp", (System.currentTimeMillis() + "").getBytes());

        baseRequestWithAmount("pray_kinddeed", args, amount, new BaseObserver(false, responseCallBack));

    }

    /**
     * 申请基金会
     *
     * @param responseCallBack
     */
    public static void applyFounder(String desc, String address, String amount, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("desc", desc.getBytes());
        args.put("address", address.getBytes());
        args.put("timestamp", (System.currentTimeMillis() + "").getBytes());

        baseRequestWithAmount("apply_founder", args, amount, new BaseObserver(false, responseCallBack));
    }

    /**
     * 申请成为寺院
     *
     * @param responseCallBack
     */
    public static void applyTemple(String unit, String creditcode, String address, String proof, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("unit", unit.getBytes());
        args.put("creditcode", creditcode.getBytes());
        args.put("address", address.getBytes());
        args.put("proof", proof.getBytes());

        baseInvokeRequest("apply_temple", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举
     *
     * @param responseCallBack
     */
    public static void addKinddeed(String name, String type, String detail, String spec, ResponseCallBack responseCallBack) {

        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", (new Random().nextInt(1000) + "").getBytes());
        args.put("name", name.getBytes());
        args.put("type", type.getBytes());
        args.put("lasttime", (System.currentTimeMillis() + "").getBytes());
        args.put("detail", detail.getBytes());
        args.put("spec", spec.getBytes());

        baseInvokeRequest("add_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举
     *
     * @param responseCallBack
     */
    public static void updateKinddeed(String kdid, String name, String type, String detail, String spec, ResponseCallBack responseCallBack) {

        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", kdid.getBytes());
        args.put("name", name.getBytes());
        args.put("type", type.getBytes());
        args.put("lasttime", (System.currentTimeMillis() + "").getBytes());
        args.put("detail", detail.getBytes());
        args.put("spec", spec.getBytes());

        baseInvokeRequest("update_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举规格
     *
     * @param hash 图片hash
     */
    public static void addKinddeedspec(String sequence, String hash, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", (new Random().nextInt(1000) + "").getBytes());
        args.put("sequence", sequence.getBytes());
        args.put("hash", hash.getBytes());

        baseInvokeRequest("add_kinddeedspec", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举描述
     *
     * @param responseCallBack
     */
    public static void addKinddeeddetail(String sequence, String desc, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", (new Random().nextInt(1000) + "").getBytes());
        args.put("sequence", sequence.getBytes());
        args.put("desc", desc.getBytes());

        baseInvokeRequest("add_kinddeeddetail", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 善举详情
     *
     * @param responseCallBack
     */
    public static void findKinddeed(String kdid, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", kdid.getBytes());

        baseQueryRequest("find_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 善举种类列表
     *
     * @param responseCallBack
     */
    public static void listKinddeedtype(ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();

        baseQueryRequest("list_kinddeedtype", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举种类
     *
     * @param responseCallBack
     */
    public static void addKinddeedtype(String id, String desc, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());
        args.put("desc", desc.getBytes());

        baseInvokeRequest("add_kinddeedtype", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 删除善举种类
     *
     * @param responseCallBack
     */
    public static void deleteKinddeedtype(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseInvokeRequest("delete_kinddeedtype", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 修改善举种类
     *
     * @param responseCallBack
     */
    public static void updateKinddeedtype(String id, String desc, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());
        args.put("desc", desc.getBytes());

        baseInvokeRequest("update_kinddeedtype", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求善举参与列表
     *
     * @param responseCallBack
     */
    public static void prayKinddeedList(String kdid, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("kdid", kdid.getBytes());

        baseQueryRequest("list_pray_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求善举评论列表
     *
     * @param responseCallBack
     */
    public static void beforeCommentKinddeedList(String kdid, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("kdid", kdid.getBytes());

        baseQueryRequest("list_beforecomment", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求善举体验标签数据列表
     *
     * @param responseCallBack
     */
    public static void commentLabelList(ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();

        baseQueryRequest("list_commentlabel", args, new BaseObserver(false, responseCallBack));
    }


    /**
     * 添加善举评论标签
     * {"kdid":"1", "satisfaction":"0", "labels":"[\"1\"]","comment":"够前评论，不孬","timestamp":"xxxxxx"}
     *
     * @param id
     * @param desc
     */
    public static void addCommentlabel(String id, String desc, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());
        args.put("desc", desc.getBytes());

        baseInvokeRequest("add_commentlabel", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加善举评论
     * {"kdid":"1", "satisfaction":"0", "labels":"[\"1\"]","comment":"够前评论，不孬","timestamp":"xxxxxx"}
     *
     * @param kdid
     * @param satisfaction 满意度
     */
    public static void addBeforecomment(String kdid, String satisfaction, String comment, String labels, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("kdid", kdid.getBytes());
        args.put("satisfaction", satisfaction.getBytes());
        args.put("comment", comment.getBytes());
        args.put("labels", labels.getBytes());
        args.put("timestamp", (System.currentTimeMillis() + "").getBytes());

        baseInvokeRequest("add_beforecomment", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 同意上架善举
     *
     * @param id
     */
    public static void approveOnlineKinddeed(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseInvokeRequest("approve_online_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 同意成为法师
     *
     * @param id
     */
    public static void approveMaster(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseInvokeRequest("approve_master", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 同意成为寺院
     *
     * @param id
     */
    public static void approveTemple(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseInvokeRequest("approve_temple", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 同意善举凭证
     *
     * @param id
     */
    public static void approveKinddeedproof(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseInvokeRequest("approve_kinddeedproof", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 查询善举订单
     *
     * @param id
     */
    public static void findPrayKinddeed(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());

        baseQueryRequest("find_pray_kinddeed", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 添加祈求善举后点评
     *
     * @param orderid
     */
    public static void addAftercomment(String orderid, String comment, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("orderid", orderid.getBytes());
        args.put("comment", comment.getBytes());
        args.put("timestamp", (System.currentTimeMillis() + "").getBytes());

        baseInvokeRequest("add_aftercomment", args, new BaseObserver(false, responseCallBack));
    }


    /**
     * 请求信用值排行榜列表
     *
     * @param responseCallBack
     */
    public static void creditrankingList(ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();

        baseQueryRequest("list_creditranking", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求功德值排行榜列表
     *
     * @param responseCallBack
     */
    public static void meritrankingList(ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();

        baseQueryRequest("list_meritranking", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求用户信用值
     *
     * @param responseCallBack
     */
    public static void findCreditranking(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());
        baseQueryRequest("find_credit", args, new BaseObserver(false, responseCallBack));
    }

    /**
     * 请求用户功德值
     *
     * @param responseCallBack
     */
    public static void findMeritranking(String id, ResponseCallBack responseCallBack) {
        HashMap<String, byte[]> args = new HashMap<>();
        args.put("id", id.getBytes());
        baseQueryRequest("find_merit", args, new BaseObserver(false, responseCallBack));
    }
}
