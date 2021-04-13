package com.chain.buddha.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.xuper.api.XuperClient;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TransferCoinActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.et_address)
    EditText mAddressEt;
    @BindView(R.id.et_amount)
    EditText mAmountEt;
    @BindView(R.id.et_psw)
    EditText mPswEt;
    @BindView(R.id.tv_balance)
    TextView mBalanceTv;
    String mBalance = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_coin);
        mTitle.setText("转账");
        getBalance();
    }

    void getBalance() {

        XuperApi.getBalanceDetails(XuperAccount.getAddress(), new ResponseCallBack<XuperClient.BalDetails[]>() {
            @Override
            public void onSuccess(XuperClient.BalDetails[] balDetails) {
                for (XuperClient.BalDetails balDetails1 : balDetails) {
                    if (!balDetails1.getFrozen()) {
                        mBalance = balDetails1.getBalance();
                        mBalanceTv.setText(mBalance);
                    }
                }
            }

            @Override
            public void onFail(String message) {
                ToastUtils.show(mContext, message);
            }
        });

    }

    @OnClick(R.id.view_submit)
    void submit() {
        String address = mAddressEt.getText().toString();
        String amount = mAmountEt.getText().toString();
        String psw = mPswEt.getText().toString();
        if (StringUtils.equalsHasNull(psw, address, amount)) {
            ToastUtils.show(mContext, "请输入完整信息");
            return;
        }
        if (StringUtils.getLongValue(amount) <= 0) {
            ToastUtils.show(mContext, "请输入金额");
            return;
        }
        if (StringUtils.getLongValue(amount) > StringUtils.getLongValue(mBalance)) {
            ToastUtils.show(mContext, "余额不足");
            return;
        }

        if (!XuperAccount.checkPsw(mContext, psw)) {
            ToastUtils.show(mContext, "密码错误");
            return;
        }

        DialogUtil.simpleDialog(mContext, "确认转出？", new DialogUtil.ConfirmCallBackObject<String>() {
            @Override
            public void onConfirmClick(String content) {

                XuperApi.transferTo(mAddressEt.getText().toString(), mAmountEt.getText().toString(), new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, "转账成功");
                        mAmountEt.setText("");
                        mPswEt.setText("");
                        getBalance();
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });

    }

    @OnClick(R.id.view_transfer_all)
    void transferAll() {
        mAmountEt.setText(mBalance);
    }

    final int REQUEST_CODE = 1;

    @OnClick(R.id.view_scan)
    void scan() {
        new RxPermissions(this).request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String qrcode = bundle.getString(CodeUtils.RESULT_STRING);
                    String[] argsList = qrcode.split("&");
                    mAddressEt.setText(argsList[0]);
                    if (argsList.length > 1) {
                        mAmountEt.setText(argsList[1]);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.show(mContext, "解析二维码失败");
                }
            }
        }
    }
}
