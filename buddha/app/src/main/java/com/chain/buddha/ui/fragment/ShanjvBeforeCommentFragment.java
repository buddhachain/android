package com.chain.buddha.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuCommentListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShanjvBeforeCommentFragment extends BaseFragment {


    @BindView(R.id.rv_qifu_list)
    RecyclerView mQifuCommentRv;

    @BindView(R.id.et_comment)
    EditText mCommentEt;
    @BindView(R.id.tv_comment)
    TextView mCommentTv;

    private QifuCommentListAdapter mQifuCommentAdapter;
    private List<String> mQifuCommentList;

    String kdid;//善举id

    public ShanjvBeforeCommentFragment(String kdid) {
        // Required empty public constructor
        this.kdid = kdid;
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_before_comment;
    }

    @Override
    protected void init() {
        Log.e("kdid", kdid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuCommentRv.setLayoutManager(layoutManager);
        mQifuCommentList = new ArrayList<>();
        mQifuCommentAdapter = new QifuCommentListAdapter(mContext, mQifuCommentList);
        mQifuCommentRv.setAdapter(mQifuCommentAdapter);
        mQifuCommentAdapter.setEmptyView(R.layout.view_empty);
        mQifuCommentAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCommentList();
            }
        });
    }

    @Override
    protected void lazyInit() {
        getCommentList();
        XuperApi.commentLabelList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");

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

    void getCommentList() {
        XuperApi.beforeCommentKinddeedList(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuCommentList.clear();
                    mQifuCommentList.addAll(Arrays.asList(list));
                    mQifuCommentList.remove(0);
                    mQifuCommentAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.tv_comment)
    void comment() {
        String labels = "[\"1\"]";
        String commnet = mCommentEt.getText().toString();
        DialogUtil.simpleDialog(mContext, "确认提交？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.addBeforecomment(kdid, "0", commnet, labels, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, resp);
                        getCommentList();
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });

    }
}
