package com.chain.buddha.ui.fragment.mine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.ExamineRenzhengListAdapter;
import com.chain.buddha.adapter.ExamineShanjvListAdapter;
import com.chain.buddha.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamineShanjvFragment extends BaseFragment {


    @BindView(R.id.rv_list)
    RecyclerView mRv;

    private ExamineShanjvListAdapter mAdapter;
    private List<String> mList;

    public ExamineShanjvFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_examine_shanjv;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        mAdapter = new ExamineShanjvListAdapter(mContext, mList);
        mRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_empty);
        mAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void lazyInit() {

        XuperApi.requestQifuList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mList.clear();
                    mList.addAll(Arrays.asList(list));
                    mList.remove(0);
                    mAdapter.notifyDataSetChanged();
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
