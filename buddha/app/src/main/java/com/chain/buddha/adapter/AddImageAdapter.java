package com.chain.buddha.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class AddImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AddImageAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_image_view, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        String hash;
        if (strings.contains(",")) {
            String[] list = strings.split(",");
            hash = list[2];
        } else {
            hash = strings;
        }
        GlideUtils.loadImageByIpfskey(getContext(), hash, baseViewHolder.getView(R.id.iv_image));
    }

}
