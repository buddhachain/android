package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.util.Log;
import android.view.View;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuRecordListAdapter;
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
 * A simple {@link Fragment} subclass.
 */
public class ShanjvListFragment extends BaseFragment {


    @BindView(R.id.rv_qifu_list)
    RecyclerView mQifuListRv;

    private QifuRecordListAdapter mQifuListAdapter;
    private List<String> mQifuListList;

    String kdid;//善举id

    public ShanjvListFragment(String kdid) {
        // Required empty public constructor
        this.kdid = kdid;
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_list;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuListRv.setLayoutManager(layoutManager);
        mQifuListList = new ArrayList<>();
        mQifuListAdapter = new QifuRecordListAdapter(mContext, mQifuListList);
        mQifuListRv.setAdapter(mQifuListAdapter);
        mQifuListAdapter.setEmptyView(R.layout.view_empty);
        mQifuListAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void lazyInit() {
        XuperApi.prayKinddeedList(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuListList.clear();
                    mQifuListList.addAll(Arrays.asList(list));
                    mQifuListList.remove(0);
                    mQifuListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                Log.e("l", message);
            }
        });
    }
}
