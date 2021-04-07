package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SaveMnemonicTipActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    String mnemonic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_mnemonic_tip);
        mTitle.setText("备份助记词安全提示");
        mnemonic = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_MNEMONIC);
    }

    @OnClick(R.id.view_goto_save)
    void check() {
        SkipInsideUtil.skipInsideActivity(mContext, CheckMnemonicActivity.class, SkipInsideUtil.SKIP_KEY_MNEMONIC, mnemonic);
    }

    @OnClick(R.id.view_not_save)
    void notCheck() {
        DialogUtil.simpleDialog(mContext, "确认不备份?", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                SkipInsideUtil.skipInsideActivity(mContext, MnemonicActivity.class, SkipInsideUtil.SKIP_KEY_MNEMONIC, mnemonic);
            }
        });
    }
}
