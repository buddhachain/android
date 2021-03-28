package com.chain.buddha.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
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

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class RenzhengTempleStep3Activity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.iv_upload_img)
    ImageView mImg;

    String unit;
    String creditcode;
    String address;
    String proof;

    List<String> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_temple_step3);
        mTitle.setText("寺院认证");

        unit = getIntent().getStringExtra("unit");
        creditcode = getIntent().getStringExtra("creditcode");
        address = getIntent().getStringExtra("address");

    }

    @OnClick(R.id.btn_next_step)
    void onClick(View view) {
        if (StringUtils.equalsNull(proof)) {
            ToastUtils.show(mContext, "请上传凭证");
            return;
        }
        DialogUtil.simpleDialog(mContext, "确认提交？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.applyTemple(unit, creditcode, address, proof, new ResponseCallBack<String>() {
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

    @OnClick(R.id.iv_upload_img)
    void upLoadImg(View view) {

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
                        GlideUtils.loadImageByIpfskey(mContext, proof, mImg);
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                }));
    }
}
