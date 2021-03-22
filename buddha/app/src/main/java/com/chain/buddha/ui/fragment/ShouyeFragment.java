package com.chain.buddha.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.xuper.api.Transaction;
import com.baidu.xuper.api.XuperClient;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.Xuper.XuperConstants;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.bean.ShanjvBean;
import com.chain.buddha.bean.XuperResponse;
import com.chain.buddha.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    protected void lazyInit() {
        XuperApi.requestQifuList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    List<String> stringList = Arrays.asList(list);
                    mQifuList = new ArrayList(stringList);
                    mQifuList.remove(0);
                    mQifuAdapter = new QifuListAdapter(mContext, mQifuList);
                    mQifuRv.setAdapter(mQifuAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
            }
        });
    }

    @OnClick(R.id.view_go_top)
    void goTop() {
        mQifuRv.smoothScrollToPosition(0);
    }
}
