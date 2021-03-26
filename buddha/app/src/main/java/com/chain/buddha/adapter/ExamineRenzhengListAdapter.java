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

public class ExamineRenzhengListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ExamineRenzhengListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_examine_renzheng_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_qifu_name, list[1]);
            baseViewHolder.setText(R.id.tv_num, list[5]);
            baseViewHolder.setText(R.id.tv_amount, list[6]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
