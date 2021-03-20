package com.chain.buddha.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.xuper.api.Transaction;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.Xuper.XuperConstants;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;

import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShouyeFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText mSearchEt;

    @BindView(R.id.lv_qidu)
    ListView mQifuLv;

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

    }

    @Override
    protected void lazyInit() {
        Transaction transaction = XuperApi.getXuperClient().queryContract(XuperAccount.getAccount(),
                XuperConstants.MODEL_NAME, XuperConstants.CONTRACT_NAME, "list_kinddeed", new HashMap<>());
        String resp = transaction.getContractResponse().getBodyStr();
        Log.e("resp", resp);

    }

    @OnClick(R.id.view_go_top)
    void goTop() {
        mQifuLv.smoothScrollToPosition(0);
    }
}
