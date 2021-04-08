package com.chain.buddha.ui.fragment.mine;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.RankingListAdapter;
import com.chain.buddha.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 信用排行
 */
public class MeritRankingFragment extends BaseFragment {


    @BindView(R.id.rv_list)
    RecyclerView mRankingListRv;

    private RankingListAdapter mRankingListAdapter;
    private List<String> mRankingList;

    public MeritRankingFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_recycleview;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRankingListRv.setLayoutManager(layoutManager);
        mRankingList = new ArrayList<>();
        mRankingListAdapter = new RankingListAdapter(mContext, mRankingList);
        mRankingListRv.setAdapter(mRankingListAdapter);
        mRankingListAdapter.setEmptyView(R.layout.view_empty);
        mRankingListAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    protected void lazyInit() {
        XuperApi.meritrankingList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mRankingList.clear();
                    mRankingList.addAll(Arrays.asList(list));
                    mRankingList.remove(0);
                    mRankingListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                Log.e("f", message);
            }
        });
    }
}
