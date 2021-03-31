package com.chain.buddha.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.AddImageAdapter;
import com.chain.buddha.adapter.AddSpecListAdapter;
import com.chain.buddha.adapter.ManageShanjvTypeAdapter;
import com.chain.buddha.adapter.ShanjvTypeAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.GifSizeFilter;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManageShanjvTypeActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    /**
     * 善举种类
     */
    @BindView(R.id.rv_type)
    RecyclerView mShanjvTypeRv;
    private List<String> mShanjvTypeList;
    private ManageShanjvTypeAdapter mManageShanjvTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shanjv_type);
        mTitle.setText("管理善举类型");
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        mShanjvTypeRv.setLayoutManager(layoutManager1);
        mShanjvTypeList = new ArrayList<>();
        mManageShanjvTypeAdapter = new ManageShanjvTypeAdapter(mContext, mShanjvTypeList);
        mShanjvTypeRv.setAdapter(mManageShanjvTypeAdapter);
        mManageShanjvTypeAdapter.addChildClickViewIds(R.id.tv_delete, R.id.tv_update);
        mManageShanjvTypeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                try {
                    String id = mShanjvTypeList.get(position).split(",")[0];

                    switch (view.getId()) {
                        case R.id.tv_delete:
                            DialogUtil.simpleDialog(mContext, "确认删除？", new DialogUtil.ConfirmCallBackInf() {
                                @Override
                                public void onConfirmClick(String content) {
                                    XuperApi.deleteKinddeedtype(id, new ResponseCallBack<String>() {
                                        @Override
                                        public void onSuccess(String resp) {
                                            ToastUtils.show(mContext, resp);
                                            getData();
                                        }

                                        @Override
                                        public void onFail(String message) {
                                            DialogUtil.tipDialog(mContext, message);
                                        }
                                    });
                                }
                            });
                            break;
                        case R.id.tv_update:
                            DialogUtil.editDialog(mContext, "修改种类", mShanjvTypeList.get(position).split(",")[1], "", new DialogUtil.ConfirmCallBackInf() {
                                @Override
                                public void onConfirmClick(String content) {
                                    XuperApi.updateKinddeedtype(id, content, new ResponseCallBack<String>() {
                                        @Override
                                        public void onSuccess(String resp) {
                                            ToastUtils.show(mContext, resp);
                                            getData();
                                        }

                                        @Override
                                        public void onFail(String message) {
                                            DialogUtil.tipDialog(mContext, message);
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });

        getData();

    }

    /**
     * 获取善举种类
     */
    void getData() {
        XuperApi.listKinddeedtype(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mShanjvTypeList.clear();
                    mShanjvTypeList.addAll(Arrays.asList(list));
                    mShanjvTypeList.remove(0);
                    mManageShanjvTypeAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

    }

    @OnClick(R.id.tv_add_type)
    void addType() {
        DialogUtil.editDialog(mContext, "添加善举种类", "", "请输入善举种类", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                String id = new Random().nextInt(1000) + "";
                try {
                    id = Integer.parseInt(mShanjvTypeList.get(mShanjvTypeList.size() - 1).split(",")[0]) + 1 + "";
                } catch (Exception e) {

                }
                XuperApi.addKinddeedtype(id, content, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, resp);
                        getData();
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });
    }


}
