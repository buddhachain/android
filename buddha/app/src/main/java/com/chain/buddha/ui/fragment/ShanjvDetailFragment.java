package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuDetailListAdapter;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.PermissionUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;

/**
 * 祈福详情
 * A simple {@link Fragment} subclass.
 */
public class ShanjvDetailFragment extends BaseFragment {


    @BindView(R.id.rv_qifu_detail)
    RecyclerView mQifuDetailRv;

    private QifuDetailListAdapter mQifuDetailAdapter;
    private List<String> mQifuDetailList;
    private List<ImageInfo> imageInfoList;

    String kdid;//善举id

    public ShanjvDetailFragment(String kdid) {
        this.kdid = kdid;
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_detail;
    }

    @Override
    protected void init() {
        Log.e("kdid", kdid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuDetailRv.setLayoutManager(layoutManager);
        mQifuDetailList = new ArrayList<>();
        mQifuDetailAdapter = new QifuDetailListAdapter(mContext, mQifuDetailList);
        mQifuDetailRv.setAdapter(mQifuDetailAdapter);
        mQifuDetailAdapter.setEmptyView(R.layout.view_empty);
        mQifuDetailAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mQifuDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ImagePreview.getInstance()
                        .setContext(mContext)
                        // 设置从第几张开始看（索引从0开始）
                        .setIndex(position)
                        //=================================================================================================
                        // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                        // 1：第一步生成的imageInfo List
                        .setImageInfoList(imageInfoList)
                        // 2：直接传url List
                        //.setImageList(List<String> imageList)

                        // 3：只有一张图片的情况，可以直接传入这张图片的url
                        //.setImage(String image)
                        //=================================================================================================

                        // 开启预览
                        .start();
            }
        });
    }

    @Override
    protected void lazyInit() {
        XuperApi.kinddeedDetail(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuDetailList.clear();
                    mQifuDetailList.addAll(Arrays.asList(list));
                    mQifuDetailList.remove(0);
                    mQifuDetailAdapter.notifyDataSetChanged();

                    imageInfoList = new ArrayList<>();
                    ImageInfo imageInfo;
                    for (String image : mQifuDetailList) {
                        image = IpfsUtils.GET_IPFS_FILE_HEAD + image.split(",")[2];
                        imageInfo = new ImageInfo();
                        imageInfo.setOriginUrl(image);// 原图url
                        imageInfo.setThumbnailUrl(image);// 缩略图url
                        imageInfoList.add(imageInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
