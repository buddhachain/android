package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.baidu.xuper.api.Account;
import com.baidu.xuper.api.XuperClient;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.Test;
import com.chain.buddha.Xuper.XuperConstants;
import com.chain.buddha.Xuper.XuperUtil;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.FileUtils;
import com.chain.buddha.utils.PermissionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        Test.test(mContext);
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        String[] permission = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean checkResult = PermissionUtils.checkPermissionsGroup(mContext, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(mContext, permission, 1);
        }
    }


    @Override
    protected void lazyInit() {
    }
}
