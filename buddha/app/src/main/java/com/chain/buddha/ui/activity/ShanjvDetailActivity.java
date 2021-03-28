package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.adapter.SpecListAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.ShanjvBeforeCommentFragment;
import com.chain.buddha.ui.fragment.ShanjvDetailFragment;
import com.chain.buddha.ui.fragment.ShanjvListFragment;
import com.chain.buddha.utils.SkipInsideUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class ShanjvDetailActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitleTv;

    @BindView(R.id.shanjv_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.shanjv_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.layout_choose_item)
    RelativeLayout mChooseItemLayout;

    @BindView(R.id.rv_spec)
    RecyclerView mSpecListRv;//购买流程列表
    private SpecListAdapter mSpecAdapter;
    private List<String> mSpecList;

    String[] mTitle;
    FmPagerAdapter mViewPagerAdapter;
    List<Fragment> mFragmentList;

    String kdid;//善举id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shanjv_detail);
        kdid = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_KDID);
        mTitleTv.setText("详情");
        mTitle = new String[]{getString(R.string.shanjv_detail), getString(R.string.shanjv_list),getString(R.string.shanjv_comment_list)};
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitle.length; i++) {
            switch (i) {
                case 0:
                    mFragmentList.add(new ShanjvDetailFragment(kdid));
                    break;
                case 1:
                    mFragmentList.add(new ShanjvListFragment(kdid));
                    break;
                case 2:
                    mFragmentList.add(new ShanjvBeforeCommentFragment(kdid));
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, false);
        mViewPagerAdapter = new FmPagerAdapter(mFragmentList, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mViewPagerAdapter);
        for (int i = 0; i < mFragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTitle[i]);
        }
        mViewPager.setOffscreenPageLimit(mFragmentList.size());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mSpecListRv.setLayoutManager(layoutManager);
        mSpecList = new ArrayList<>();
        mSpecAdapter = new SpecListAdapter(mContext, mSpecList);
        mSpecListRv.setAdapter(mSpecAdapter);

        getData();
    }

    void getData() {
//        XuperApi.kinddeedDetail(kdid, new ResponseCallBack<String>() {
//            @Override
//            public void onSuccess(String resp) {
//                Log.e("resp", resp);
//            }
//
//            @Override
//            public void onFail(String message) {
//
//            }
//        });
        XuperApi.kinddeedSpec(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mSpecList.clear();
                    mSpecList.addAll(Arrays.asList(list));
                    mSpecList.remove(0);
                    mSpecAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.findKinddeed(kdid, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }


    @OnClick({R.id.btn_open_choose, R.id.btn_close_choose, R.id.btn_next_step})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_choose:
                //开始选择项目
                mChooseItemLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close_choose:
                mChooseItemLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_next_step:
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(SkipInsideUtil.SKIP_KEY_NUM, mSpecAdapter.getNum());
                hashMap.put(SkipInsideUtil.SKIP_KEY_SPEC, mSpecList.get(mSpecAdapter.getSelectPosition()));
                hashMap.put(SkipInsideUtil.SKIP_KEY_KDID, kdid);
                SkipInsideUtil.skipInsideActivity(mContext, AddgdzActivity.class, hashMap);
                break;
            default:
                break;
        }
    }
}
