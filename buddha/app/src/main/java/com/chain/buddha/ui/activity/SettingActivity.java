package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTitle.setText("设置");
    }

    @OnClick(R.id.reset_psw)
    void resetPsw() {
        SkipInsideUtil.skipInsideActivity(mContext, ResetPswActivity.class);
    }

    @OnClick(R.id.get_mnemonic)
    void getMnemonic() {
        DialogUtil.checkPswDialog(mContext, new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                SkipInsideUtil.skipInsideActivity(mContext, ShowMnemonicTipActivity.class,
                        SkipInsideUtil.SKIP_KEY_MNEMONIC, XuperAccount.getAccount().getMnemonic());
            }
        }, null);
    }

    @OnClick(R.id.view_delete_wallet)
    void deleteWallet() {
        DialogUtil.simpleDialog(mContext, "确认删除？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperAccount.logoutAccount(mContext);
            }
        });
    }

}
