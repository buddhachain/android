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
import com.chain.buddha.adapter.ShanjvProveListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.SendShanjvProveActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 上传凭证
 */
public class MySendProveFragment extends BaseFragment {

    @BindView(R.id.rv_qifu_list)
    RecyclerView mQifuListRv;

    private ShanjvProveListAdapter mQifuListAdapter;
    private List<String> mQifuListList;

    public MySendProveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_my_send_prove;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuListRv.setLayoutManager(layoutManager);
        mQifuListList = new ArrayList<>();
        mQifuListAdapter = new ShanjvProveListAdapter(mContext, mQifuListList);
        mQifuListRv.setAdapter(mQifuListAdapter);
        mQifuListAdapter.setEmptyView(R.layout.view_empty);
        mQifuListAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mQifuListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String[] list = mQifuListList.get(position).split(",");
                SkipInsideUtil.skipInsideActivity(mContext, SendShanjvProveActivity.class, SkipInsideUtil.SKIP_KEY_ID, list[0]);
            }
        });
    }

    @Override
    protected void lazyInit() {
        XuperApi.prayKinddeedList("", new ResponseCallBack<String>() {
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
