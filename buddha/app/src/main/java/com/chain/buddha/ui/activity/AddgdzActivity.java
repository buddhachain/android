package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加功德主页面
 */
public class AddgdzActivity extends BaseActivity {

    int num = 0;
    String spec, kdid;

    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgdz);
        num = getIntent().getIntExtra(SkipInsideUtil.SKIP_KEY_NUM, 1);
        spec = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_SPEC);
        kdid = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_KDID);
        initView();
    }

    @OnClick({R.id.btn_next_step})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_step:
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(SkipInsideUtil.SKIP_KEY_NUM, num);
                hashMap.put(SkipInsideUtil.SKIP_KEY_SPEC, spec);
                hashMap.put(SkipInsideUtil.SKIP_KEY_KDID, kdid);
                SkipInsideUtil.skipInsideActivity(context, PaymentOrderActivity.class, hashMap);
                break;
            default:
                break;
        }
    }

    void initView() {
        String[] specInfo = spec.split(",");
        int totalPrice = num * StringUtils.getIntValue(specInfo[3]);
        mTotalPriceTv.setText("合计:" + totalPrice + "xuper");
    }


}
