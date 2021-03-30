package com.chain.buddha.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.GlideUtils;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heshuai on 2018/9/25.
 */

public class QifuListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    /**
     * 善举种类
     */
    private HashMap<String, String> mShanjvTypeMap;

    public QifuListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_qifu_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String strings) {
        try {
            String[] list = strings.split(",");
            baseViewHolder.setText(R.id.tv_qifu_title, list[1]);

            GlideUtils.loadImageByIpfskey(getContext(), list[2], baseViewHolder.getView(R.id.iv_qifu), R.mipmap.qifu_default_cover);
            if (mShanjvTypeMap != null && mShanjvTypeMap.containsKey(list[3])) {
                baseViewHolder.setGone(R.id.tv_tag1, false);
                baseViewHolder.setText(R.id.tv_tag1, mShanjvTypeMap.get(list[3]));
            } else {
                baseViewHolder.setGone(R.id.tv_tag1, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setShanjvTypeMap(HashMap<String, String> mShanjvTypeMap) {
        this.mShanjvTypeMap = mShanjvTypeMap;
        notifyDataSetChanged();
    }
}
