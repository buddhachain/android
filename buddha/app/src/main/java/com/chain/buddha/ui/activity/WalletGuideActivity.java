package com.chain.buddha.ui.activity;

import android.os.Bundle;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.OnClick;

public class WalletGuideActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_guide);
    }

    @OnClick(R.id.view_register)
    void register() {
        SkipInsideUtil.skipInsideActivity(mContext, RegisterActivity.class);
        finish();
    }

    @OnClick(R.id.view_login)
    void login() {
        SkipInsideUtil.skipInsideActivity(mContext, LoginActivity.class);
        finish();
    }


}
