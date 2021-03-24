package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.crypto.account.ECDSAAccount;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.io.File;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.et_mnemonic)
    EditText mMnemonicEt;
    @BindView(R.id.et_psw)
    EditText mPswEt;
    @BindView(R.id.et_psw_2)
    EditText mPsw2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTitle.setText("导入钱包");
        mMnemonicEt.setText("单 写 横 浙 乌 轴 语 云 缓 三 找 购 峰 侦 法 使 勃 雪");
    }

    @OnClick(R.id.view_submit)
    void submit() {
        String mnemonic = mMnemonicEt.getText().toString();
        String psw = mPswEt.getText().toString();
        String psw2 = mPsw2Et.getText().toString();
        if (StringUtils.equalsHasNull(mnemonic, psw, psw2)) {
            ToastUtils.show(context, "请输入完整信息");
            return;
        }
        if (!StringUtils.equals(psw, psw2)) {
            ToastUtils.show(context, "两次密码输入不一致");
            return;
        }
        try {
            int language = StringUtils.isChinese(mnemonic.charAt(0)) ? 1 : 2;
            ECDSAAccount ecdsaAccount = new ECDSAAccount();
            ecdsaAccount.createByMnemonic(mnemonic, language);
            new File(XuperAccount.getAccountCachePath(context)).delete();
            ecdsaAccount.saveToFile(XuperAccount.getAccountCachePath(context), psw2);
            Account account = Account.retrieve(mnemonic, language);
            XuperAccount.setAccount(account);
            ToastUtils.show(context, "导入成功");
            finish();
        } catch (Throwable e) {
            DialogUtil.tipDialog(context, "导入失败，请检查助记词");
        }
    }


    @OnClick(R.id.view_register)
    void register() {
        SkipInsideUtil.skipInsideActivity(context, RegisterActivity.class);
        finish();
    }
}
