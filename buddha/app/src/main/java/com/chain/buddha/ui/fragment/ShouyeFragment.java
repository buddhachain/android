package com.chain.buddha.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.ShanjvDetailActivity;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.SkipInsideUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShouyeFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText mSearchEt;

    @BindView(R.id.rv_qifu)
    RecyclerView mQifuRv;

    @BindView(R.id.view_go_top)
    View mGoTopView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private QifuListAdapter mQifuAdapter;
    private List<String> mQifuList;


    public ShouyeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_shouye;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuRv.setLayoutManager(layoutManager);
        mQifuList = new ArrayList<>();
        mQifuAdapter = new QifuListAdapter(mContext, mQifuList);
        mQifuRv.setAdapter(mQifuAdapter);
        mQifuAdapter.setEmptyView(R.layout.view_empty);
        mQifuAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh();
            }
        });
        // 设置点击事件
        mQifuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                try {
                    String[] list = mQifuList.get(position).split(",");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBeans.LoginEvent event) {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyInit() {
        refreshLayout.autoRefresh();
    }

    void getData() {
        XuperApi.requestQifuList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuList.clear();
                    mQifuList.addAll(Arrays.asList(list));
                    mQifuList.remove(0);
                    mQifuAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFail(String message) {
                refreshLayout.finishRefresh();
            }
        });
    }

    @OnClick(R.id.view_go_top)
    void goTop() {
        mQifuRv.smoothScrollToPosition(0);
    }

}
