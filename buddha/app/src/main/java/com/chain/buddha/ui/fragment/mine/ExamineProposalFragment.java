package com.chain.buddha.ui.fragment.mine;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.ExamineProposalListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.MakeProposalActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提案审核列表
 * A simple {@link Fragment} subclass.
 */
public class ExamineProposalFragment extends BaseFragment {

    @BindView(R.id.rv_list)
    RecyclerView mRv;

    private ExamineProposalListAdapter mAdapter;
    private List<String> mList;

    public ExamineProposalFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_proposal_list;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        mAdapter = new ExamineProposalListAdapter(mContext, mList);
        mRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_empty);
        mAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                try {
                    String[] list = mList.get(position).split(",");

                    if (StringUtils.equals(list[2], "0")) {
                        //未通过审核
                        approve(list[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    protected void lazyInit() {
        XuperApi.proposalList(new ResponseCallBack<String>() {
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

    /**
     * 同意提案
     */
    void approve(String id) {
//        XuperApi.approveProposal(id, new ResponseCallBack<String>() {
//            @Override
//            public void onSuccess(String resp) {
//                ToastUtils.show(mContext, resp);
//                lazyInit();
//            }
//
//            @Override
//            public void onFail(String message) {
//                DialogUtil.tipDialog(mContext, message);
//            }
//        });
    }

    @OnClick(R.id.tv_make_proposal)
    void makeProposal() {
        SkipInsideUtil.skipInsideActivity(mContext, MakeProposalActivity.class);
    }

}
