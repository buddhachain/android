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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.AddImageAdapter;
import com.chain.buddha.adapter.AddSpecListAdapter;
import com.chain.buddha.adapter.ShanjvTypeAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.GifSizeFilter;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.SkipInsideUtil;
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

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendShanjvActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_shanjv_name)
    EditText mNameEt;
    @BindView(R.id.et_shanjv_desc)
    EditText mDescEt;

    @BindView(R.id.rv_spec)
    RecyclerView mSpecRv;
    private AddSpecListAdapter mSpecAdapter;
    private List<String> mSpecList;

    @BindView(R.id.rv_image)
    RecyclerView mImgRv;
    private AddImageAdapter mAddImageAdapter;
    private List<String> mImageList;

    @BindView(R.id.tv_manage_type)
    TextView mManageTypeTv;
    @BindView(R.id.sp_shanjv_type)
    Spinner mShanjvTypeSp;
    /**
     * 善举种类
     */
    private List<String> mShanjvTypeList;
    private String mSelectShanjvType = "1";
    private ShanjvTypeAdapter mShanjvTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_shanjv);
        mTitle.setText("上架善举");
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        mSpecRv.setLayoutManager(layoutManager1);
        mSpecList = new ArrayList<>();
        mSpecAdapter = new AddSpecListAdapter(mContext, mSpecList);
        mSpecRv.setAdapter(mSpecAdapter);
        // 设置点击事件
        mSpecAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                DialogUtil.simpleDialog(mContext, "确认删除该规格？", new DialogUtil.ConfirmCallBackObject<String>() {
                    @Override
                    public void onConfirmClick(String content) {
                        mSpecList.remove(position);
                        mSpecAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mImgRv.setLayoutManager(layoutManager);
        mImageList = new ArrayList<>();
        mAddImageAdapter = new AddImageAdapter(mContext, mImageList);
        mImgRv.setAdapter(mAddImageAdapter);

        // 设置点击事件
        mAddImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                DialogUtil.simpleDialog(mContext, "确认删除该张图片？", new DialogUtil.ConfirmCallBackObject<String>() {
                    @Override
                    public void onConfirmClick(String content) {
                        mImageList.remove(position);
                        mAddImageAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        getData();
        mShanjvTypeList = new ArrayList<>();
        mShanjvTypeAdapter = new ShanjvTypeAdapter(mContext, mShanjvTypeList);
        mShanjvTypeSp.setAdapter(mShanjvTypeAdapter);
        mShanjvTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try {
                    String[] list = mShanjvTypeList.get(pos).split(",");
                    mSelectShanjvType = list[0];
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_JJH) {
            mManageTypeTv.setVisibility(View.VISIBLE);
            mManageTypeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipInsideUtil.skipInsideActivity(mContext, ManageShanjvTypeActivity.class);
                }
            });
        } else {
            mManageTypeTv.setVisibility(View.GONE);
        }
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
                    mShanjvTypeAdapter.notifyDataSetChanged();
                    mSelectShanjvType = mShanjvTypeList.get(0).split(",")[0];
                    mShanjvTypeSp.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick(R.id.tv_submit)
    void submit() {
        if (!mSpecAdapter.checkInput()) {
            ToastUtils.show(mContext, "请填写完善规格信息");
            return;
        }
        String name = mNameEt.getText().toString();
        List<HashMap<String, String>> detailList = new ArrayList<>();
        for (int i = 1; i <= mImageList.size(); i++) {
            String hash = mImageList.get(i - 1);
            HashMap<String, String> item = new HashMap<>();
            item.put("sequence", i + "");
            item.put("hash", hash);
            detailList.add(item);
        }
        String detail = new Gson().toJson(detailList);
        String spec = mSpecAdapter.getSpecString();
        DialogUtil.simpleDialog(mContext, "确认上传？", new DialogUtil.ConfirmCallBackObject<String>() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.addKinddeed(name, mSelectShanjvType, detail, spec, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, resp);
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });
    }


    int REQUEST_CODE_CHOOSE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<String> mSelected = Matisse.obtainPathResult(data);
            if (mSelected.size() > 0) {
                showLoadingDialog("上传中", "");
                upLoadFile(mSelected);
            }
        }
    }


    void upLoadFile(List<String> filePathList) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                try {
                    emitter.onNext(IpfsUtils.uploadFileList(filePathList));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<List<String>>() {
                    @Override
                    public void onSuccess(List<String> hashList) {
                        mImageList.addAll(hashList);
                        mAddImageAdapter.notifyDataSetChanged();
                        hideLoadingDialog();
                    }

                    @Override
                    public void onFail(String message) {
                        hideLoadingDialog();
                        DialogUtil.tipDialog(mContext, message);
                    }

                }));
    }

    @OnClick(R.id.tv_add_image)
    void addImage() {
        Matisse.from(mContext)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dimen_224))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.chain.buddha.fileProvider"))
                .showPreview(false) // Default is `true`
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @OnClick(R.id.tv_add_spec)
    void addSpec() {
        if (!mSpecAdapter.checkInput()) {
            ToastUtils.show(mContext, "请填写完善规格信息");
            return;
        }
        mSpecList.add("");
        mSpecAdapter.notifyDataSetChanged();
    }
}
