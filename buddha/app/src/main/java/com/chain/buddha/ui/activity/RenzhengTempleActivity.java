package com.chain.buddha.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.RenzhengTempleStep1Fragment;
import com.chain.buddha.ui.fragment.RenzhengTempleStep2Fragment;
import com.chain.buddha.ui.fragment.RenzhengTempleStep3Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RenzhengTempleActivity extends BaseActivity {

    @BindView(R.id.renzheng_frameLayout)
    FrameLayout frameLayout;

    List<Fragment> fragmentList;
    private int mIndex=0;

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
    }

    @OnClick(R.id.btn_next_step)
    protected void onClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mIndex<fragmentList.size()-1){
            ft.replace(R.id.renzheng_frameLayout,fragmentList.get(mIndex+1));
            mIndex++;
        }

    }
}
