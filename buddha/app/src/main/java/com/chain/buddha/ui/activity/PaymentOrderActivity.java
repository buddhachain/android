package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentOrderActivity extends BaseActivity {

    int num = 0;
    String spec, kdid;

    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTv;
    @BindView(R.id.order_price)
    TextView mPriceTv;
    @BindView(R.id.order_number)
    TextView mNumTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_order);
        num = getIntent().getIntExtra(SkipInsideUtil.SKIP_KEY_NUM, 1);
        spec = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_SPEC);
        kdid = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_KDID);
        initView();
    }


    @OnClick(R.id.tv_submit)
    void submit() {
        String[] specInfo = spec.split(",");
        int price = StringUtils.getIntValue(specInfo[3]);
        int totalPrice = num * price;
        XuperApi.prayKinddeed(specInfo[0], num + "", kdid, totalPrice + "", new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
            }

            @Override
            public void onFail(String message) {
                DialogUtil.tipDialog(context, message);
            }
        });
    }

    void initView() {
        String[] specInfo = spec.split(",");
        int price = StringUtils.getIntValue(specInfo[3]);
        int totalPrice = num * price;
        mTotalPriceTv.setText("合计:" + totalPrice + "xuper");
        mPriceTv.setText(price + "");
        mNumTv.setText(num + "");
    }
}
