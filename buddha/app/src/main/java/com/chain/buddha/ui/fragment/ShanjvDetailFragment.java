package com.chain.buddha.ui.fragment;

import android.Manifest;
import android.util.Log;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.utils.PermissionUtils;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShanjvDetailFragment extends BaseFragment {


    String kdid;//善举id

    public ShanjvDetailFragment(String kdid) {
        this.kdid = kdid;
        // Required empty public constructor
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_shanjv_detail;
    }

    @Override
    protected void init() {
        Log.e("kdid", kdid);
    }

    @Override
    protected void lazyInit() {
    }
}
