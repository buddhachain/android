package com.chain.buddha.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 购买流程列表adapter
 * Created by heshuai on 2018/9/25.
 */

public class ManageShanjvTypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ManageShanjvTypeAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_manage_shanjv_type, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_type_name, list[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
