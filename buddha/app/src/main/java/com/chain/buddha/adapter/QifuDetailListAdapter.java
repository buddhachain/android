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

public class QifuDetailListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public QifuDetailListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_qifu_detail_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_qifu_title, list[1]);

            GlideUtils.loadImageByIpfskey(getContext(), list[2], baseViewHolder.getView(R.id.iv_qifu));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
