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

public class BookListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public BookListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_book_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_name, list[0]);
            baseViewHolder.setText(R.id.tv_value, list[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
