package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MnemonicActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.tv_mnemonic)
    TextView mMnemonicTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemonic);
        mTitle.setText("保存助记词");
        mMnemonicTv.setText(getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_MNEMONIC));
    }

    @OnClick(R.id.view_submit)
    void submit() {
        DialogUtil.simpleDialog(mContext, "确认保存好助记词", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                finish();
            }
        }, null);
    }
}
