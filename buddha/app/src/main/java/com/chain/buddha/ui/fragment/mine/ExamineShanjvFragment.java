package com.chain.buddha.ui.fragment.mine;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.ExamineShanjvListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                try {
                    String[] list = mList.get(position).split(",");

                    if (StringUtils.equals(list[5], "1")) {
                        //正在审核
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

    /**
     * 同意上架善举
     */
    void approve(String id) {
        XuperApi.approveOnlineKinddeed(id, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                ToastUtils.show(mContext, resp);
            }

            @Override
            public void onFail(String message) {
                DialogUtil.tipDialog(mContext, message);
            }
        });
    }
}
