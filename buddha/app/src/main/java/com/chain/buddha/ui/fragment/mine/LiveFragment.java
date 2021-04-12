package com.chain.buddha.ui.fragment.mine;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.LiveListAdapter;
import com.chain.buddha.adapter.LiveListAdapter;
import com.chain.buddha.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 信用排行
 */
public class LiveFragment extends BaseFragment {


    @BindView(R.id.rv_list)
    RecyclerView mLiveListRv;

    private LiveListAdapter mLiveListAdapter;
    private List<String> mLiveList;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_recycleview;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mLiveListRv.setLayoutManager(layoutManager);
        mLiveList = new ArrayList<>();
        mLiveListAdapter = new LiveListAdapter(mContext, mLiveList);
        mLiveListRv.setAdapter(mLiveListAdapter);
        mLiveListAdapter.setEmptyView(R.layout.view_empty);
        mLiveListAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    protected void lazyInit() {

    }
}
