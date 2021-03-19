package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.ShanjvDetailFragment;
import com.chain.buddha.ui.fragment.ShanjvListFragment;
import com.chain.buddha.utils.SkipInsideUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShanjvDetailActivity extends BaseActivity {

    @BindView(R.id.shanjv_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.shanjv_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.layout_choose_item)
    RelativeLayout mChooseItemLayout;

    String[] mTitle;
    FmPagerAdapter mViewPagerAdapter;
    List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shanjv_detail);
        mTitle=new String[]{getString(R.string.shanjv_detail),getString(R.string.shanjv_list)};
        mFragmentList=new ArrayList<>();
        for (int i = 0; i < mTitle.length; i++) {
            switch (i){
                case 0:
                    mFragmentList.add(new ShanjvDetailFragment());
                    break;
                case 1:
                    mFragmentList.add(new ShanjvListFragment());
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager,false);
        mViewPagerAdapter=new FmPagerAdapter(mFragmentList,getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mViewPagerAdapter);
        for (int i = 0; i < mFragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTitle[i]);
        }
    }


    @OnClick({R.id.btn_open_choose,R.id.btn_close_choose,R.id.btn_next_step})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.btn_open_choose:
                //开始选择项目
                mChooseItemLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close_choose:
                mChooseItemLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_next_step:
                SkipInsideUtil.skipInsideActivity(context,AddgdzActivity.class);
                break;
            default:break;
        }
    }
}
