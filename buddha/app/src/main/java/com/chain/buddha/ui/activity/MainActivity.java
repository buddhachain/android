package com.chain.buddha.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.FoyouFragment;
import com.chain.buddha.ui.fragment.MineFragment;
import com.chain.buddha.ui.fragment.QifuFragment;
import com.chain.buddha.ui.fragment.ShouyeFragment;
import com.chain.buddha.ui.fragment.XiuxingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_fragment)
    FrameLayout mFragmentLayout;
    @BindView(R.id.bottom_bar)
    BottomNavigationView mBottomBar;
    int mIndex = 0;
    Fragment[] mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomBar.setItemIconTintList(null);
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        XuperAccount.checkAccount(mContext);
                    }
                });

        mFragment = new Fragment[5];
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (savedInstanceState != null) {
            int len = mFragment.length;
            for (int i = 0; i < len; i++) {
                mFragment[i] = fragmentManager.findFragmentByTag("f" + i);
                transaction.hide(mFragment[i]);
                transaction.setMaxLifecycle(mFragment[i], Lifecycle.State.STARTED);
            }
        } else {
            mFragment[0] = new ShouyeFragment();
            mFragment[1] = new QifuFragment();
            mFragment[2] = new XiuxingFragment();
            mFragment[3] = new FoyouFragment();
            mFragment[4] = new MineFragment();
            for (int i = 0; i < mFragment.length; i++) {
                transaction.add(R.id.main_fragment, mFragment[i], "f" + i);
                transaction.hide(mFragment[i]);
                transaction.setMaxLifecycle(mFragment[i], Lifecycle.State.STARTED);
            }
        }
        transaction.show(mFragment[0]);
        transaction.setMaxLifecycle(mFragment[0], Lifecycle.State.RESUMED);
        transaction.commit();


        mBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int index = 0;

                switch (menuItem.getItemId()) {
                    case R.id.bottom_menu_shouye:
                        index = 0;
                        break;
                    case R.id.bottom_menu_qifu:
                        index = 1;
                        break;
                    case R.id.bottom_menu_xiuxing:
                        index = 2;
                        break;
                    case R.id.bottom_menu_foyou:
                        index = 3;
                        break;
                    case R.id.bottom_menu_mine:
                        index = 4;
                        break;
                    default:
                        break;
                }
                if (index != mIndex) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.hide(mFragment[mIndex]).show(mFragment[index]).commit();
                    ft.setMaxLifecycle(mFragment[mIndex], Lifecycle.State.STARTED);
                    ft.setMaxLifecycle(mFragment[index], Lifecycle.State.RESUMED);
                    mIndex = index;
                }

                return true;
            }
        });

    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
