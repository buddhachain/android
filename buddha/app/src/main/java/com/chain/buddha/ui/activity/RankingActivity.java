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
import com.chain.buddha.ui.fragment.mine.MeritRankingFragment;
import com.chain.buddha.ui.fragment.mine.MyAllOrderFragment;
import com.chain.buddha.ui.fragment.mine.MyEvaluateFragment;
import com.chain.buddha.ui.fragment.mine.MyExamineFragment;
import com.chain.buddha.ui.fragment.mine.MyProcessingFragment;
import com.chain.buddha.ui.fragment.mine.MySendProveFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 排行榜
 */
public class RankingActivity extends BaseActivity {

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
        setContentView(R.layout.activity_ranking);
//        int index = getIntent().getExtras().getInt("position");
        int index = 0;
        mTitle.setText("排行榜");
        mTabs = new String[]{"信用值排行", "功德值排行"};
        fragmentList = new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            switch (i) {
                case 0:
                    fragmentList.add(new CreditRankingFragment());
                    break;
                case 1:
                    fragmentList.add(new MeritRankingFragment());
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
