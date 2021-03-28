package com.chain.buddha.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.GifSizeFilter;
import com.chain.buddha.utils.GlideUtils;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RenzhengMasterActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_creditcode)
    EditText mEtCreditcode;

    @BindView(R.id.iv_proof)
    ImageView mProofIv;

    private String proof;
    List<String> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_master);
        mTitle.setText("法师认证");
        mProofIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matisse.from(mContext)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .maxSelectable(1)
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
        });
    }

    @OnClick(R.id.view_submit)
    void applyMaster() {
        if (StringUtils.equalsNull(proof)) {
            ToastUtils.show(mContext, "请上传凭证");
            return;
        }
        String creditcode = mEtCreditcode.getText().toString();
        XuperApi.applyMaster(creditcode, proof, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtils.show(mContext, s);
            }

            @Override
            public void onFail(String message) {
                DialogUtil.tipDialog(mContext, message);
            }
        });
    }


    int REQUEST_CODE_CHOOSE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected.size() > 0) {
                String path = mSelected.get(0);
                upLoadFile(path);
            }
        }
    }

    void upLoadFile(String filePath) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext(IpfsUtils.uploadFile(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        proof = s;
                        ToastUtils.show(mContext, "上传成功");
                        GlideUtils.loadImageByIpfskey(mContext, proof, mProofIv);
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                }));
    }
}
