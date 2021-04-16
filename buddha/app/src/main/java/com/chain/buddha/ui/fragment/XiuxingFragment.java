package com.chain.buddha.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.fragment.mine.CreditRankingFragment;
import com.chain.buddha.ui.fragment.mine.MeritRankingFragment;
import com.chain.buddha.ui.fragment.xiuxing.CangshugeFragment;
import com.chain.buddha.ui.fragment.xiuxing.FanyinFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class XiuxingFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    List<Fragment> fragmentList;
    String[] mTabs;
    FmPagerAdapter adapter;

    public XiuxingFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_xiuxing;
    }

    @Override
    protected void init() {
        int index = 0;
        mTabs = new String[]{"藏书阁", "梵音", "祈福"};
        fragmentList = new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            switch (i) {
                case 0:
                    fragmentList.add(new CangshugeFragment());
                    break;
                case 1:
                    fragmentList.add(new FanyinFragment());
                    break;
                case 2:
                    fragmentList.add(new QifuFragment());
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, false);
        adapter = new FmPagerAdapter(fragmentList, getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < fragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTabs[i]);
        }
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void lazyInit() {

    }
}
