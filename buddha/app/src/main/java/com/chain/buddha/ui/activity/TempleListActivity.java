package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.ManageShanjvTypeAdapter;
import com.chain.buddha.adapter.TempleListAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class TempleListActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    /**
     * 善举种类
     */
    @BindView(R.id.rv_temple)
    RecyclerView mTempleRv;
    private List<String> mTempleList;
    private TempleListAdapter mTempleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_list);
        mTitle.setText("寺院列表");
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        mTempleRv.setLayoutManager(layoutManager1);
        mTempleList = new ArrayList<>();
        mTempleListAdapter = new TempleListAdapter(mContext, mTempleList);
        mTempleRv.setAdapter(mTempleListAdapter);
        mTempleListAdapter.addChildClickViewIds(R.id.tv_delete, R.id.tv_update);
        mTempleListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String id = mTempleList.get(position).split(",")[0];
                SkipInsideUtil.skipInsideActivity(mContext, TempleInfoActivity.class, SkipInsideUtil.SKIP_KEY_ID, id);
            }

        });

        getData();

    }

    /**
     * 获取寺院列表
     */
    void getData() {
        XuperApi.requestTempleList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mTempleList.clear();
                    mTempleList.addAll(Arrays.asList(list));
                    mTempleList.remove(0);
                    mTempleListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                Log.e("message", message);
            }
        });
        XuperApi.templeMasterList(null, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
            }

            @Override
            public void onFail(String message) {
                Log.e("message", message);
            }
        });
    }

}
