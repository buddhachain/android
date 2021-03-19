package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加功德主页面
 */
public class AddgdzActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgdz);
    }

    @OnClick({R.id.btn_next_step})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.btn_next_step:
                SkipInsideUtil.skipInsideActivity(context,PaymentOrderActivity.class);
                break;
            default:break;
        }
    }



}
