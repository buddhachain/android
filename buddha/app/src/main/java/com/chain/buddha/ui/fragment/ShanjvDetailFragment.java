package com.chain.buddha.ui.fragment;

import android.Manifest;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.PermissionUtils;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShanjvDetailFragment extends BaseFragment {


    public ShanjvDetailFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_detail;
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
