package com.chain.buddha.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class ActionRecordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ActionRecordAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_action_record, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_action_name, list[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
