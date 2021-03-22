package com.chain.buddha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class MasterListAdapter extends BaseQuickAdapter<List<String> , BaseViewHolder>  {


    public MasterListAdapter(int layoutResId, @Nullable List<List<String>> data) {
        super(R.layout.item_qifu_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, List<String> strings) {

    }



}
