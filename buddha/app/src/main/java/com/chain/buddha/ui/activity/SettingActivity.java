package com.chain.buddha.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.ShareUtils;
import com.chain.buddha.utils.SkipInsideUtil;
import com.mob.MobSDK;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTitle.setText("设置");
    }

    @OnClick(R.id.reset_psw)
    void resetPsw() {
        SkipInsideUtil.skipInsideActivity(mContext, ResetPswActivity.class);
    }

    @OnClick(R.id.get_mnemonic)
    void getMnemonic() {
        DialogUtil.checkPswDialog(mContext, new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                SkipInsideUtil.skipInsideActivity(mContext, ShowMnemonicTipActivity.class,
                        SkipInsideUtil.SKIP_KEY_MNEMONIC, XuperAccount.getAccount().getMnemonic());
            }
        }, null);
    }

    @OnClick(R.id.view_delete_wallet)
    void deleteWallet() {
        DialogUtil.simpleDialog(mContext, "确认删除？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperAccount.logoutAccount(mContext);
            }
        });
    }

    @OnClick(R.id.share_app)
    void shareApp() {
//        OnekeyShare oks = new OnekeyShare();
//// title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle(getString(R.string.share_title));
//// titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("https://www.pgyer.com/Cn5y");
//// text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//// setImageUrl是网络图片的url
//        oks.setImageUrl("http://i1.sinaimg.cn/IT/2010/0419/201041993740.jpg");
//// url在微信、Facebook等平台中使用
//        oks.setUrl("https://www.pgyer.com/Cn5y");
//// 启动分享GUI
//        oks.show(MobSDK.getContext());
        ShareUtils.shareText(mContext, "这是一段分享的文字");
    }


}
