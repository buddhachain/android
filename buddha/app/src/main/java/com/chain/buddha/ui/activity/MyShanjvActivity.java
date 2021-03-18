package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.mine.MyAllOrderFragment;
import com.chain.buddha.ui.fragment.mine.MyEvaluateFragment;
import com.chain.buddha.ui.fragment.mine.MyExamineFragment;
import com.chain.buddha.ui.fragment.mine.MyProcessingFragment;
import com.chain.buddha.ui.fragment.mine.MySendProveFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyShanjvActivity extends BaseActivity {

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
        setContentView(R.layout.activity_my_shanjv);
        int index = getIntent().getExtras().getInt("myshanjv");
        mTitle.setText("我的善举");
        mTabs = new String[]{"处理中", "上传凭证", "审核", "待评价", "全部订单"};
        fragmentList=new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            switch (i) {
                case 0:
                    fragmentList.add(new MyProcessingFragment());
                    break;
                case 1:
                    fragmentList.add(new MySendProveFragment());
                    break;
                case 2:
                    fragmentList.add(new MyExamineFragment());
                    break;
                case 3:
                    fragmentList.add(new MyEvaluateFragment());
                    break;
                case 4:
                    fragmentList.add(new MyAllOrderFragment());
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager,false);
        adapter=new FmPagerAdapter(fragmentList,getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < fragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTabs[i]);
        }
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(index);
    }
}
