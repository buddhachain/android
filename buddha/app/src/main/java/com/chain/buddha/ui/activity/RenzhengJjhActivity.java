package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;

import butterknife.BindView;

public class RenzhengJjhActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzhengjjh);
        mTitle.setText("基金会成员认证");
    }
}
