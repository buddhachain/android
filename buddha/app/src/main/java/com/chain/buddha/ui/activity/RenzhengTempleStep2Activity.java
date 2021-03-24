package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RenzhengTempleStep2Activity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_unit)
    EditText mUnitEt;
    @BindView(R.id.et_creditcode)
    EditText mCreditcodeEt;
    @BindView(R.id.et_address)
    EditText mAddressEtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_temple_step2);
        mTitle.setText("寺院认证");
    }

    @OnClick(R.id.btn_next_step)
    void onClick(View view) {
        String unit = mUnitEt.getText().toString();
        String creditcode = mCreditcodeEt.getText().toString();
        String address = mAddressEtv.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("unit", unit);
        params.put("creditcode", creditcode);
        params.put("address", address);
        SkipInsideUtil.skipInsideActivity(mContext, RenzhengTempleStep3Activity.class, params);
    }

}
