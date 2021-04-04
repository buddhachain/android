package com.chain.buddha.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class KeywordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public KeywordAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_keyword, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String keyWord) {
        baseViewHolder.setText(R.id.tv_keyword, keyWord);
    }

}
