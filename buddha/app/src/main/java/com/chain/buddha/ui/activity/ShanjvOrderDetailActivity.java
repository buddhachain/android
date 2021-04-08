package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ShanjvOrderDetailActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_comment)
    EditText mCommentEt;

    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shanjv_order_detail);
        mTitle.setText("订单详情");
        orderId = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_ID);

        getData();
    }

    private void getData() {
        XuperApi.findPrayKinddeed(orderId, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("tag", resp);
            }

            @Override
            public void onFail(String message) {
                ToastUtils.show(mContext, message);
            }
        });
    }

    @OnClick(R.id.tv_comment)
    void comment() {
        String labels = "[\"1\"]";
        String commnet = mCommentEt.getText().toString();
        DialogUtil.simpleDialog(mContext, "确认提交？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.addAftercomment(orderId, commnet, new ResponseCallBack<String>() {
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
