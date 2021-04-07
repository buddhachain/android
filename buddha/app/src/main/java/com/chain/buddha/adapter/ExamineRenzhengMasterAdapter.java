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

public class ExamineRenzhengMasterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ExamineRenzhengMasterAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_examine_renzheng_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_name, list[1]);
            baseViewHolder.setText(R.id.tv_owner, list[0]);
            if (StringUtils.equals(list[3], "1")) {
                //已通过审核
                baseViewHolder.setText(R.id.tv_approve, "已通过");
                baseViewHolder.setBackgroundResource(R.id.tv_approve, R.color.color_white);
            } else {
                baseViewHolder.setText(R.id.tv_approve, "未审核");
                baseViewHolder.setBackgroundResource(R.id.tv_approve, R.color.color_gray_8e);
            }
            baseViewHolder.getView(R.id.tv_proof).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String profImage = IpfsUtils.GET_IPFS_FILE_HEAD + list[2];
                    ImagePreview.getInstance()
                            .setContext(getContext())
                            // 设置从第几张开始看（索引从0开始）
                            .setIndex(0)
                            .setImage(profImage)
                            // 开启预览
                            .start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
