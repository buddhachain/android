package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.util.Log;
import android.view.View;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuDetailListAdapter;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.PermissionUtils;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 祈福详情
 * A simple {@link Fragment} subclass.
 */
public class ShanjvDetailFragment extends BaseFragment {


    @BindView(R.id.rv_qifu_detail)
    RecyclerView mQifuDetailRv;

    private QifuDetailListAdapter mQifuDetailAdapter;
    private List<String> mQifuDetailList;

    String kdid;//善举id

    public ShanjvDetailFragment(String kdid) {
        this.kdid = kdid;
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_detail;
    }

    @Override
    protected void init() {
        Log.e("kdid", kdid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuDetailRv.setLayoutManager(layoutManager);
        mQifuDetailList = new ArrayList<>();
        mQifuDetailAdapter = new QifuDetailListAdapter(mContext, mQifuDetailList);
        mQifuDetailRv.setAdapter(mQifuDetailAdapter);
        mQifuDetailAdapter.setEmptyView(R.layout.view_empty);
        mQifuDetailAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void lazyInit() {
        XuperApi.kinddeedDetail(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuDetailList.clear();
                    mQifuDetailList.addAll(Arrays.asList(list));
                    mQifuDetailList.remove(0);
                    mQifuDetailAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
