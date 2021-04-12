package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.mine.CreditRankingFragment;
import com.chain.buddha.ui.fragment.mine.LiveFragment;
import com.chain.buddha.ui.fragment.mine.MeritRankingFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 排行榜
 */
public class LiveActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    List<Fragment> fragmentList;
    String[] mTabs;
    FmPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_main);
//        int index = getIntent().getExtras().getInt("position");
        int index = 0;
        mTitle.setText("直播");
        mTabs = new String[]{"直播", "关注"};
        fragmentList = new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            switch (i) {
                case 0:
                    fragmentList.add(new LiveFragment());
                    break;
                case 1:
                    fragmentList.add(new LiveFragment());
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, false);
        adapter = new FmPagerAdapter(fragmentList, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < fragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTabs[i]);
        }
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.setCurrentItem(index);
    }
}
