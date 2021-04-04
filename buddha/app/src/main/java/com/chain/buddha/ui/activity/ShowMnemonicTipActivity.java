package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowMnemonicTipActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    String mnemonic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mnemonic_tip);
        mTitle.setText("导出助记词安全提示");
        mnemonic = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_MNEMONIC);
    }

    @OnClick(R.id.view_goto_show)
    void mnemonic() {
        SkipInsideUtil.skipInsideActivity(mContext, MnemonicActivity.class, SkipInsideUtil.SKIP_KEY_MNEMONIC, mnemonic);
        finish();
    }


    @OnClick(R.id.view_not_show)
    void notShow() {
        finish();
    }

}
