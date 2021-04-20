package com.chain.buddha.ui.activity.xiuxing;

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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 排行榜
 */
public class FanyinPlayerActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanyin_player);
        mTitle.setText("梵音");
//        MusicPlayerManager.getInstance().init(getApplicationContext()).initialize(mContext);
    }
}
