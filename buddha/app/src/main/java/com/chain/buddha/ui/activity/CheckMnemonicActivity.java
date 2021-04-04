package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.adapter.KeywordAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;

public class CheckMnemonicActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    String mnemonic;
    @BindView(R.id.rv_keywords)
    RecyclerView mTopKeyWordsRv;
    List<String> topKeywordList;
    KeywordAdapter mTopAdapter;

    @BindView(R.id.rv_select_keywords)
    RecyclerView mBottomKeyWordsRv;
    List<String> bottomKeywordList;
    KeywordAdapter mBottomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mnemonic);
        mTitle.setText("验证助记词");
        mnemonic = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_MNEMONIC);
        if (StringUtils.equalsNull(mnemonic)) {
            ToastUtils.show(mContext, "没有助记词");
            finish();
        }

        int spanCount = StringUtils.isChinese(mnemonic.charAt(0)) ? 4 : 3;
        mTopKeyWordsRv.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        topKeywordList = new ArrayList<>();
        mTopAdapter = new KeywordAdapter(mContext, topKeywordList);
        mTopKeyWordsRv.setAdapter(mTopAdapter);
        mTopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String keyword = topKeywordList.remove(position);
                bottomKeywordList.add(keyword);
                mTopAdapter.notifyDataSetChanged();
                mBottomAdapter.notifyDataSetChanged();
            }
        });

        mBottomKeyWordsRv.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        bottomKeywordList = new ArrayList<>();
        bottomKeywordList.addAll(Arrays.asList(mnemonic.split(" ")));
        Collections.shuffle(bottomKeywordList);
        mBottomAdapter = new KeywordAdapter(mContext, bottomKeywordList);
        mBottomKeyWordsRv.setAdapter(mBottomAdapter);
        mBottomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String keyword = bottomKeywordList.remove(position);
                topKeywordList.add(keyword);
                mTopAdapter.notifyDataSetChanged();
                mBottomAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.view_submit)
    void submit() {

        String selectKeyWords = "";
        for (String key : topKeywordList) {
            selectKeyWords += key;
        }

        if (StringUtils.equals(selectKeyWords, mnemonic.replace(" ", ""))) {
            finish();
        } else {
            ToastUtils.show(mContext, "助记词不一致，请检查");
        }
    }
}
