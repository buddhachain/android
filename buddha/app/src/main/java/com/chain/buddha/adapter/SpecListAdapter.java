package com.chain.buddha.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 购买流程列表adapter
 * Created by heshuai on 2018/9/25.
 */

public class SpecListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectPosition = 0;
    private int num = 1;

    public SpecListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_spec_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            baseViewHolder.getView(R.id.view_top).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectPosition == baseViewHolder.getLayoutPosition()) {
                        return;
                    }
                    selectPosition = baseViewHolder.getLayoutPosition();
                    num = 1;
                    notifyDataSetChanged();
                }
            });
            String[] list = strings.split(",");
            int price = StringUtils.getIntValue(list[3]);
            baseViewHolder.setText(R.id.tv_spec_context, list[2]);
            if (selectPosition == baseViewHolder.getLayoutPosition()) {
                baseViewHolder.setText(R.id.tv_spec_price, price * num + "");
                baseViewHolder.setText(R.id.tv_num, num + "");
            } else {
                baseViewHolder.setText(R.id.tv_spec_price, price + "");
                baseViewHolder.setText(R.id.tv_num, "1");
            }
            if (selectPosition == baseViewHolder.getLayoutPosition()) {
                baseViewHolder.setGone(R.id.view_price, false);
            } else {
                baseViewHolder.setGone(R.id.view_price, true);
            }
            baseViewHolder.getView(R.id.tv_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (num <= 1) {
                        return;
                    }
                    num -= 1;
                    notifyDataSetChanged();
                }
            });
            baseViewHolder.getView(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    num += 1;
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public int getNum() {
        return num;
    }
}
