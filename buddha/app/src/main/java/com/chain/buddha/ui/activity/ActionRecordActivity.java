package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.ActionRecordAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 操作记录
 */
public class ActionRecordActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;


    @BindView(R.id.rv_record)
    RecyclerView mRecordRv;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private ActionRecordAdapter mAdapter;
    private List<String> mRecordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_record);
        mTitle.setText("明细列表");
        getData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecordRv.setLayoutManager(layoutManager);
        mRecordList = new ArrayList<>();
        mAdapter = new ActionRecordAdapter(mContext, mRecordList);
        mRecordRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_empty);
        mAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh();
            }
        });
        // 设置点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                try {
                    String[] list = mRecordList.get(position).split(",");
                    String kdid = list[0];
                    SkipInsideUtil.skipInsideActivity(mContext, ShanjvDetailActivity.class, SkipInsideUtil.SKIP_KEY_KDID, kdid);
                } catch (Exception e) {

                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    void getData() {
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
