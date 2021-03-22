package com.chain.buddha.ui.fragment;

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
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QifuFragment extends BaseFragment {

    @BindView(R.id.rv_qifu)
    RecyclerView mQifuRv;


    private QifuListAdapter mQifuAdapter;
    private List<String> mQifuList;

    public QifuFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_qifu;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuRv.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();
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
}
