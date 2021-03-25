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


    public QifuListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_qifu_list, data);
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
