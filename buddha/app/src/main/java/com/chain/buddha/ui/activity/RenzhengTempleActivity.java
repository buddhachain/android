package com.chain.buddha.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.mine.RenzhengTempleStep1Fragment;
import com.chain.buddha.ui.fragment.mine.RenzhengTempleStep2Fragment;
import com.chain.buddha.ui.fragment.mine.RenzhengTempleStep3Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RenzhengTempleActivity extends BaseActivity {

    @BindView(R.id.renzheng_frameLayout)
    FrameLayout frameLayout;

    List<Fragment> fragmentList;
    private int mIndex=0;

    @BindView(R.id.text_back)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_temple);
        fragmentList=new ArrayList<Fragment>(){{
            add(new RenzhengTempleStep1Fragment());
            add(new RenzhengTempleStep2Fragment());
            add(new RenzhengTempleStep3Fragment());
        }};
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            ft.add(R.id.renzheng_frameLayout,fragmentList.get(i)).hide(fragmentList.get(i));
        }
        ft.show(fragmentList.get(0));
        mTitle.setText("寺院认证");
    }

    @OnClick(R.id.btn_next_step)
     void onClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mIndex<fragmentList.size()-1){
            ft.replace(R.id.renzheng_frameLayout,fragmentList.get(mIndex+1)).commit();
            mIndex++;
        }

    }
}
