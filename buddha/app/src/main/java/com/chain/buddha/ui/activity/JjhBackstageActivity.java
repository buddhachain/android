package com.chain.buddha.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.mine.ExamineActivityFragment;
import com.chain.buddha.ui.fragment.mine.ExamineRenzhengFragment;
import com.chain.buddha.ui.fragment.mine.ExamineShanjvFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class JjhBackstageActivity extends BaseActivity {

    @BindView(R.id.jjh_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.jjh_viewPager)
    ViewPager mViewPager;
    List<Fragment> fragmentList;
    String[] mTitle;
    FmPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjh_backstage);
        mTitle=new String[]{"上架审核","活动审核","认证审核"};
        fragmentList=new ArrayList<>();
        for (int i = 0; i < mTitle.length; i++) {
            switch (i){
                case 0:
                    fragmentList.add(new ExamineShanjvFragment());
                    break;
                case 1:
                    fragmentList.add(new ExamineActivityFragment());
                    break;
                case 2:
                    fragmentList.add(new ExamineRenzhengFragment());
                    break;
                default:break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager,false);
        adapter=new FmPagerAdapter(fragmentList,getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < fragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTitle[i]);
        }

    }
}
