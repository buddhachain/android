package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.ShareUtils;
import com.chain.buddha.utils.SkipInsideUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.reset_psw)
    View mResetPsw;

    @BindView(R.id.get_mnemonic)
    View mGetMnemonic;

    @BindView(R.id.view_delete_wallet)
    TextView mDeleteWallet;

    private boolean isHasAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTitle.setText("设置");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    void refreshView() {

        isHasAccount = XuperAccount.ifHasAccount(mContext);
        mResetPsw.setVisibility(isHasAccount ? View.VISIBLE : View.GONE);
        mGetMnemonic.setVisibility(isHasAccount ? View.VISIBLE : View.GONE);
        mDeleteWallet.setText(isHasAccount ? "删除钱包" : "创建钱包");
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
        if (isHasAccount) {
            DialogUtil.simpleDialog(mContext, "确认删除？", new DialogUtil.ConfirmCallBackInf() {
                @Override
                public void onConfirmClick(String content) {
                    XuperAccount.logoutAccount(mContext);
                    refreshView();
                }
            });
        } else {
            SkipInsideUtil.skipInsideActivity(mContext, WalletGuideActivity.class);
        }
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
