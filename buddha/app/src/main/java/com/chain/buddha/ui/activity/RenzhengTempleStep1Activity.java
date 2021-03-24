package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RenzhengTempleStep1Activity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_temple_step1);
        mTitle.setText("寺院认证");
    }

    @OnClick(R.id.btn_next_step)
    void onClick(View view) {
        SkipInsideUtil.skipInsideActivity(mContext, RenzhengTempleStep2Activity.class);
    }

}
