package com.chain.buddha.ui.fragment.xiuxing;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.BookListAdapter;
import com.chain.buddha.adapter.RankingListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.xiuxing.BookReaderActivity;
import com.chain.buddha.utils.SkipInsideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 信用排行
 */
public class CangshugeFragment extends BaseFragment {


    @BindView(R.id.rv_list)
    RecyclerView mBookListRv;

    private BookListAdapter mBookListAdapter;
    private List<String> mBookList;

    public CangshugeFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_recycleview;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBookListRv.setLayoutManager(layoutManager);
        mBookList = new ArrayList<>();
        mBookList.add("[0,1]");
        mBookListAdapter = new BookListAdapter(mContext, mBookList);
        mBookListRv.setAdapter(mBookListAdapter);
        mBookListAdapter.setEmptyView(R.layout.view_empty);
        mBookListAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipInsideUtil.skipInsideActivity(mContext, BookReaderActivity.class);
            }
        });

    }

    @Override
    protected void lazyInit() {
        XuperApi.creditrankingList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mBookList.clear();
                    mBookList.addAll(Arrays.asList(list));
                    mBookList.remove(0);
                    mBookListAdapter.notifyDataSetChanged();
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
