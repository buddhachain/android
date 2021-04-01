package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.xuper.api.Account;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.et_psw)
    EditText mPswEt;
    @BindView(R.id.et_psw_2)
    EditText mPsw2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTitle.setText("创建钱包");
    }

    @OnClick(R.id.view_submit)
    void submit() {
        String psw = mPswEt.getText().toString();
        String psw2 = mPsw2Et.getText().toString();
        if (StringUtils.equalsHasNull(psw, psw2)) {
            ToastUtils.show(mContext, "请输入完整信息");
            return;
        }
        if (StringUtils.equals(psw, psw2)) {
            ToastUtils.show(mContext, "两次密码输入不一致");
            return;
        }
        try {
            new File(XuperAccount.getAccountCachePath(mContext)).delete();

        } catch (Throwable e) {

        }
        Account account = Account.createAndSave(XuperAccount.getAccountCachePath(mContext), psw2, 1, 1);
        SkipInsideUtil.skipInsideActivity(mContext, MnemonicActivity.class, SkipInsideUtil.SKIP_KEY_MNEMONIC, account.getMnemonic());
    }
}
