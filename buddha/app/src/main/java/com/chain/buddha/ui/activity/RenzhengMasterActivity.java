package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RenzhengMasterActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_creditcode)
    EditText mEtCreditcode;

    @BindView(R.id.et_proof)
    EditText mEtProof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_master);
        mTitle.setText("法师认证");
    }

    @OnClick(R.id.view_submit)
    void applyMaster() {
        String creditcode = mEtCreditcode.getText().toString();
        String proof = mEtProof.getText().toString();
        XuperApi.applyMaster(creditcode, proof, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtils.show(mContext, s);
            }

            @Override
            public void onFail(String message) {
                DialogUtil.tipDialog(mContext, message);
            }
        });
    }
}
