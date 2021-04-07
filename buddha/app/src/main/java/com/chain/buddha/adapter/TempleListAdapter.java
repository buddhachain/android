package com.chain.buddha.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cc.shinichi.library.ImagePreview;

/**
 * Created by heshuai on 2018/9/25.
 */

public class TempleListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TempleListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_temple_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_name, list[1]);
            baseViewHolder.setText(R.id.tv_creditcode, list[2]);
            baseViewHolder.setText(R.id.tv_address, list[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
