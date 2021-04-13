package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chain.buddha.R;
import com.chain.buddha.adapter.KeywordAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MnemonicActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.rv_keywords)
    RecyclerView mKeyWordsRv;
    String mnemonic;
    List<String> keywordList;
    KeywordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemonic);
        mTitle.setText("保存助记词");
        mnemonic = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_MNEMONIC);
        if (StringUtils.equalsNull(mnemonic)) {
            ToastUtils.show(mContext, "没有助记词");
            finish();
        }

        int spanCount = StringUtils.isChinese(mnemonic.charAt(0)) ? 4 : 3;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mKeyWordsRv.setLayoutManager(layoutManager);
        keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(mnemonic.split(" ")));
        mAdapter = new KeywordAdapter(mContext, keywordList);
        mKeyWordsRv.setAdapter(mAdapter);
    }

    @OnClick(R.id.view_submit)
    void submit() {
        DialogUtil.simpleDialog(mContext, "确认保存好助记词", new DialogUtil.ConfirmCallBackObject<String>() {
            @Override
            public void onConfirmClick(String content) {
                SkipInsideUtil.skipInsideActivity(mContext, CheckMnemonicActivity.class, SkipInsideUtil.SKIP_KEY_MNEMONIC, mnemonic);
                finish();
            }
        });
    }
}
