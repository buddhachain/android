package com.chain.buddha.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chain.buddha.utils.UIUtils;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class BaseActivity extends AppCompatActivity {

    public Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
//        ImmersionBar.with(this).init();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //butterknife activity里面不需要解绑
        ButterKnife.bind(this);
    }

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        UIUtils.finish(activity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UIUtils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
