package com.chain.buddha.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chain.buddha.R;
import com.chain.buddha.utils.UIUtils;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class BaseActivity extends AppCompatActivity {

    public Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //butterknife activity里面不需要解绑
        ButterKnife.bind(this);
        try {
            ImmersionBar.with(this).statusBarDarkFont(true)  //状态栏字体是深色，不写默认为亮色
                    .titleBarMarginTop(R.id.title_view)
                    .init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
