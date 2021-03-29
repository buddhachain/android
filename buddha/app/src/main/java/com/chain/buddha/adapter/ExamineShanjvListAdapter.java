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

public class ExamineShanjvListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ExamineShanjvListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_examine_shanjv_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_qifu_name, list[1]);
            baseViewHolder.setText(R.id.tv_owner, list[2]);
            if (StringUtils.equals(list[5], "1")) {
                //正在申请状态
                baseViewHolder.setText(R.id.tv_approve, "未审核");
                baseViewHolder.setBackgroundResource(R.id.tv_approve, R.color.color_gray_8e);
            } else {
                baseViewHolder.setText(R.id.tv_approve, "未申请");
                baseViewHolder.setBackgroundResource(R.id.tv_approve, R.color.color_white);
            }
            if (StringUtils.equals(list[6], "1")) {
                //已上线
                baseViewHolder.setText(R.id.tv_state, "已上架");
            } else {
                baseViewHolder.setText(R.id.tv_state, "未上架");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
