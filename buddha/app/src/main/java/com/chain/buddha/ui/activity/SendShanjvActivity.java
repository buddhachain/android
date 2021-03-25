package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class SendShanjvActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_shanjv_name)
    EditText mNameEt;
    @BindView(R.id.et_shanjv_desc)
    EditText mDescEt;
    @BindView(R.id.et_shanjv_price)
    EditText mPriceEt;
    @BindView(R.id.et_shanjv_num)
    EditText mNumEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_shanjv);
        mTitle.setText("上架善举");
    }

    @OnClick(R.id.tv_submit)
    void submit() {
        String name = mNameEt.getText().toString();
        String detail = "[{\"sequence\":\"1\", \"hash\":\"Qmf62UYvhAdZ2Qs19ZXd2F9TgMB3a39NLZdkDV4ea6vR1b\"},{\"sequence\":\"2\", \"hash\":\"Qmf62UYvhAdZ2Qs19ZXd2F9TgMB3a39NLZdkDV4ea6vR1b-IMG_20200720_065751.jpg\"}]";
        String spec = "[{\"sequence\":\"1\", \"desc\":\"祈福观音\", \"price\":\"10\"},{\"sequence\":\"2\", \"desc\":\"祈福\", \"price\":\"10\"}]";
        DialogUtil.simpleDialog(mContext, "确认上传？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.addKinddeed(name, "1", detail, spec, new ResponseCallBack<String>() {
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
