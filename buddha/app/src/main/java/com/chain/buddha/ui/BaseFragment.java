package com.chain.buddha.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    private View view;
    protected Activity mContext;

    private boolean isLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setLayout(), null);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 绑定布局
     *
     * @return
     */
    protected abstract int setLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /**
     * 初始化组件
     */
    protected abstract void init();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded) {
            lazyInit();
            isLoaded = true;
        }

    }


    protected abstract void lazyInit();

}
