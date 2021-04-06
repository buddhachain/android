package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.xuper.api.Account;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPswActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.et_old_psw)
    EditText mOldPswEt;
    @BindView(R.id.et_psw)
    EditText mPswEt;
    @BindView(R.id.et_psw_2)
    EditText mPsw2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psw);
        if (!XuperAccount.ifLoginAccount()) {
            ToastUtils.show(mContext, "请先打开钱包");
            finish();
        }
        mTitle.setText("修改密码");
    }

    @OnClick(R.id.view_submit)
    void submit() {
        String oldPsw = mOldPswEt.getText().toString();
        String psw = mPswEt.getText().toString();
        String psw2 = mPsw2Et.getText().toString();
        if (StringUtils.equalsHasNull(oldPsw, psw, psw2)) {
            ToastUtils.show(mContext, "请输入完整信息");
            return;
        }
        if (!StringUtils.equals(psw, psw2)) {
            ToastUtils.show(mContext, "两次密码输入不一致");
            return;
        }
        Account account = XuperAccount.getAccountFromFile(mContext, psw);
        if (account == null) {
            ToastUtils.show(mContext, "旧密码错误");
            return;
        }
        try {
            XuperAccount.saveAccount(mContext, account.getMnemonic(), psw2);
            ToastUtils.show(mContext, "修改成功");
            finish();
        } catch (Throwable e) {
            DialogUtil.tipDialog(mContext, "修改成功");
        }
    }

}
