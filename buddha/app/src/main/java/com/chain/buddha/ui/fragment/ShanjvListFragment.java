package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.Test;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShanjvListFragment extends BaseFragment {


    public ShanjvListFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_list;
    }

    @Override
    protected void init() {

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
