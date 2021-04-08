package com.chain.buddha.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class MasterListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public MasterListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_master_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_name, list[1]);
            if (StringUtils.equals(list[2], "0")) {//未批准
                baseViewHolder.setText(R.id.tv_state, "未批准");
                baseViewHolder.setTextColorRes(R.id.tv_state, R.color.color_white);
                baseViewHolder.setBackgroundResource(R.id.tv_state, R.color.base_color);
            } else {
                baseViewHolder.setText(R.id.tv_state, "已批准");
                baseViewHolder.setTextColorRes(R.id.tv_state, R.color.base_color);
                baseViewHolder.setBackgroundResource(R.id.tv_state, R.color.transparent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
