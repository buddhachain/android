package com.chain.buddha.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.QifuListAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.ShanjvDetailActivity;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.SkipInsideUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.vlive.ui.main.LiveMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShouyeFragment extends BaseFragment {


    @BindView(R.id.sv)
    NestedScrollView mSv;

    @BindView(R.id.et_search)
    EditText mSearchEt;

    @BindView(R.id.rv_qifu)
    RecyclerView mQifuRv;

    @BindView(R.id.view_go_top)
    View mGoTopView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.banner)
    Banner mBanner;

    private QifuListAdapter mQifuAdapter;
    private List<String> mQifuList;
    private List<Integer> mBannerImgList;

    /**
     * 善举种类
     */
    private HashMap<String, String> mShanjvTypeMap;


    public ShouyeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_shouye;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mQifuRv.setLayoutManager(layoutManager);
        mQifuList = new ArrayList<>();
        mQifuAdapter = new QifuListAdapter(mContext, mQifuList);
        mQifuRv.setAdapter(mQifuAdapter);
        mQifuAdapter.setEmptyView(R.layout.view_empty);
        mQifuAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh();
            }
        });
        // 设置点击事件
        mQifuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                try {
                    String[] list = mQifuList.get(position).split(",");
                    String kdid = list[0];
                    SkipInsideUtil.skipInsideActivity(mContext, ShanjvDetailActivity.class, SkipInsideUtil.SKIP_KEY_KDID, kdid);
                } catch (Exception e) {

                }
            }
        });

        mSv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("scrollY", scrollY + "");
                if (scrollY > 1000) {
                    mGoTopView.setVisibility(View.VISIBLE);
                } else {
                    mGoTopView.setVisibility(View.GONE);
                }

            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });

        mBannerImgList = new ArrayList<>();
        mBannerImgList.add(R.mipmap.img_banner);
        mBannerImgList.add(R.mipmap.img_banner);
        mBanner.setAdapter(new BannerImageAdapter<Integer>(mBannerImgList) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer img, int position, int size) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                        .load(img)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(mContext));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBeans.LoginEvent event) {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyInit() {
        refreshLayout.autoRefresh();
    }

    void getData() {
        XuperApi.requestQifuList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mQifuList.clear();
                    mQifuList.addAll(Arrays.asList(list));
                    mQifuList.remove(0);
                    // 反转lists
                    Collections.reverse(mQifuList);
                    mQifuAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFail(String message) {
                refreshLayout.finishRefresh();
            }
        });

        XuperApi.listKinddeedtype(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mShanjvTypeMap = new HashMap<>();
                    for (String itemType : list) {
                        if (!itemType.contains(",")) {
                            continue;
                        }
                        String[] temp = itemType.split(",");
                        mShanjvTypeMap.put(temp[0], temp[1]);
                    }

                    mQifuAdapter.setShanjvTypeMap(mShanjvTypeMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
            }
        });
    }

    @OnClick(R.id.view_go_top)
    void goTop() {
        mSv.smoothScrollTo(0, 0);
    }

    @OnClick(R.id.view_live)
    void goLive() {
        SkipInsideUtil.skipInsideActivity(mContext, LiveMainActivity.class);
//        SkipInsideUtil.skipInsideActivity(mContext, LiveRoomActivity.class);
    }

}
