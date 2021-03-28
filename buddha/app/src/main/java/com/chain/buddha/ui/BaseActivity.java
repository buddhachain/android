package com.chain.buddha.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.chain.buddha.R;
import com.chain.buddha.utils.UIUtils;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class BaseActivity extends AppCompatActivity {

    public Activity mContext;
    protected ProgressDialog mProgressLoadingDialog;

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
                    .keyboardEnable(true)
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


    protected void showLoadingDialog(String title, String msg) {
        try {
//            if (StringUtils.equalsNull(msg)) {
//                msg = getString(R.string.loading);
//            }
            if (mProgressLoadingDialog == null) {
                mProgressLoadingDialog = new ProgressDialog(this);
                final LayoutInflater inflater = LayoutInflater.from(this);
                final View view = inflater.inflate(R.layout.dialog_loading, null);
                mProgressLoadingDialog.setView(view);
            }

            if (!mProgressLoadingDialog.isShowing()) {
                mProgressLoadingDialog.setTitle(title);
                mProgressLoadingDialog.setMessage(msg);
                mProgressLoadingDialog.show();
            } else {
                mProgressLoadingDialog.setTitle(title);
                mProgressLoadingDialog.setMessage(msg);
            }

        } catch (Exception e) {

        }
    }

    protected void hideLoadingDialog() {

        if (mProgressLoadingDialog != null) {
            if (mProgressLoadingDialog.isShowing()) {
                mProgressLoadingDialog.cancel();
            }
        }

    }
}
