package com.chain.buddha.adapter;

import android.content.Context;
import android.text.format.DateUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.Constants;
import com.chain.buddha.R;
import com.chain.buddha.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class QifuCommentListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public QifuCommentListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_qifu_comment_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_user_name, list[0]);
            baseViewHolder.setText(R.id.tv_comment, list[4]);
            String satisfaction = Constants.SATISFACTION_TYPE_MAP.get(list[2]);
            baseViewHolder.setText(R.id.tv_satisfaction, satisfaction == null ? "" : satisfaction);
            baseViewHolder.setText(R.id.tv_labels, list[3]);
            baseViewHolder.setText(R.id.tv_timestamp, TimeUtils.timeStamp2Date(list[5]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
