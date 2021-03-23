package com.chain.buddha.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.ui.fragment.FoyouFragment;
import com.chain.buddha.ui.fragment.MineFragment;
import com.chain.buddha.ui.fragment.QifuFragment;
import com.chain.buddha.ui.fragment.ShouyeFragment;
import com.chain.buddha.ui.fragment.XiuxingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbruyelle.rxpermissions3.RxPermissions;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.function.Consumer;

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
//        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(granted -> {
//                    if (granted) { // Always true pre-M
//                        // I can control the camera now
//                    } else {
//                        // Oups permission denied
//                    }
//                });

//        new RxPermissions(this).

        XuperAccount.checkAccount(context);
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
}
