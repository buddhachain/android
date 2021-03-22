package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.MasterListAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MasterListActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;


    @BindView(R.id.lv_master)
    ListView mMasterLv;

    MasterListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);
        mTitle.setText("法师列表");
        initData();
    }

    void initData() {
        XuperApi.requestMasterList(new ResponseCallBack() {
            @Override
            public void onSuccess(Object o) {
                Log.e("o", o.toString());
            }

            @Override
            public void onFail(String message) {

            }
        });

    }


}
