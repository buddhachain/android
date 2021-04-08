package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.view.View;
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

public class RenzhengJjhActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_name)
    EditText mNameTv;
    @BindView(R.id.et_address)
    EditText mAddressTv;
    @BindView(R.id.et_amount)
    EditText mAmountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzhengjjh);
        mTitle.setText("基金会成员认证");
    }


    @OnClick(R.id.btn_next_step)
    void onClick(View view) {
        String desc = mNameTv.getText().toString();
        String address = mAddressTv.getText().toString();
        String amount = mAmountTv.getText().toString();

        DialogUtil.simpleDialog(mContext, "确认提交？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.applyFounder(desc, address, amount, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, resp);
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });
    }
}
