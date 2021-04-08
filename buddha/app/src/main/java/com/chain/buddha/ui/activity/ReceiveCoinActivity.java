package com.chain.buddha.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.ToastUtils;
import com.chain.buddha.utils.UIUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ReceiveCoinActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.et_amount)
    EditText mAmountEt;
    @BindView(R.id.tv_my_address)
    TextView mAddressTv;
    String qrCodeString;

    @BindView(R.id.iv_qrcode)
    ImageView mQrImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_coin);
        mTitle.setText("收款");

        mAddressTv.setText(XuperAccount.getAddress());
        qrCodeString = XuperAccount.getAddress();
        showQrcode();
        mAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                qrCodeString = XuperAccount.getAddress() + "&" + mAmountEt.getText().toString();
                showQrcode();
            }
        });
    }

    @OnClick(R.id.view_copy)
    void copyAddress() {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("address", XuperAccount.getAddress());
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.show(mContext, "复制成功");

    }

    void showQrcode() {
        mQrImg.setImageBitmap(CodeUtils.createImage(qrCodeString, UIUtils.dip2px(mContext, 150), UIUtils.dip2px(mContext, 150), null));

    }
}
