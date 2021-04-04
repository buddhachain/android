package com.chain.buddha.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.ActionRecordActivity;
import com.chain.buddha.ui.activity.JjhBackstageActivity;
import com.chain.buddha.ui.activity.LoginActivity;
import com.chain.buddha.ui.activity.MasterBackstageActivity;
import com.chain.buddha.ui.activity.MyShanjvActivity;
import com.chain.buddha.ui.activity.ReceiveCoinActivity;
import com.chain.buddha.ui.activity.RenzhengJjhActivity;
import com.chain.buddha.ui.activity.RenzhengMasterActivity;
import com.chain.buddha.ui.activity.RenzhengTempleStep1Activity;
import com.chain.buddha.ui.activity.SendShanjvActivity;
import com.chain.buddha.ui.activity.SettingActivity;
import com.chain.buddha.ui.activity.TempleBackstageActivity;
import com.chain.buddha.ui.activity.TransferCoinActivity;
import com.chain.buddha.ui.activity.WalletGuideActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;
import com.chain.buddha.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_my_address)
    TextView mMyAddressTv;

    @BindView(R.id.tv_my_balance)
    TextView mMyBalanceTv;

    @BindView(R.id.user_name)
    TextView mNickNameTv;

    @BindView(R.id.tv_login)
    TextView mLoginTv;

    @BindView(R.id.tv_fashi_state)
    TextView mFashiStateTv;

    @BindView(R.id.tv_siyuan_state)
    TextView mSiyuanStateTv;

    @BindView(R.id.tv_jjh_state)
    TextView mJjhStateTv;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBeans.LoginEvent event) {
        requestData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyInit() {
//        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    void refreshView() {
        if (XuperAccount.ifLoginAccount()) {
            mNickNameTv.setText(getString(R.string.normal));
            mNickNameTv.setVisibility(View.VISIBLE);
            mLoginTv.setText(getString(R.string.logout));
            mLoginTv.setVisibility(View.GONE);
        } else if (XuperAccount.ifHasAccount(mContext)) {
            mNickNameTv.setText(getString(R.string.normal));
            mNickNameTv.setVisibility(View.GONE);
            mLoginTv.setText(getString(R.string.open_wallet));
            mLoginTv.setVisibility(View.VISIBLE);
        } else {
            mNickNameTv.setText(getString(R.string.not_login));
            mNickNameTv.setVisibility(View.GONE);
            mLoginTv.setText(getString(R.string.login));
            mLoginTv.setVisibility(View.VISIBLE);
        }

        switch (XuperAccount.getAccountType()) {
            case XuperAccount.ACCOUNT_TYPE_FASHI:
                mNickNameTv.setText(getString(R.string.fashi));
                mFashiStateTv.setText(R.string.has_approve);
                mFashiStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.base_color));
                mJjhStateTv.setText(R.string.no_approve);
                mJjhStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mSiyuanStateTv.setText(R.string.no_approve);
                mSiyuanStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                break;
            case XuperAccount.ACCOUNT_TYPE_SIYUAN:
                mNickNameTv.setText(getString(R.string.siyuan));
                mFashiStateTv.setText(R.string.no_approve);
                mFashiStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mJjhStateTv.setText(R.string.no_approve);
                mJjhStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mSiyuanStateTv.setText(R.string.has_approve);
                mSiyuanStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.base_color));
                break;
            case XuperAccount.ACCOUNT_TYPE_JJH:
                mNickNameTv.setText(getString(R.string.jijinhui));
                mFashiStateTv.setText(R.string.no_approve);
                mFashiStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mJjhStateTv.setText(R.string.has_approve);
                mJjhStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.base_color));
                mSiyuanStateTv.setText(R.string.no_approve);
                mSiyuanStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                break;
            default:
                mFashiStateTv.setText(R.string.no_approve);
                mFashiStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mJjhStateTv.setText(R.string.no_approve);
                mJjhStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                mSiyuanStateTv.setText(R.string.no_approve);
                mSiyuanStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_level2));
                break;
        }
        if (!StringUtils.equalsNull(XuperAccount.getAddress())) {
            mMyAddressTv.setText(XuperAccount.getAddress());
        } else {
            mMyAddressTv.setText("暂无地址");
        }

    }

    void requestData() {

        XuperApi.getBalance(XuperAccount.getAddress(), new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String balance) {
                mMyBalanceTv.setText(balance);
            }

            @Override
            public void onFail(String message) {
                ToastUtils.show(mContext, message);
            }
        });

        XuperApi.getAccountByAK(XuperAccount.getAddress(), new ResponseCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsFounder(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                if (!resp.contains("not")) {
                    XuperAccount.setAccountType(XuperAccount.ACCOUNT_TYPE_JJH);
                    mNickNameTv.setText(getString(R.string.jijinhui));
                    refreshView();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsMaster(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                if (!resp.contains("not")) {
                    XuperAccount.setAccountType(XuperAccount.ACCOUNT_TYPE_FASHI);
                    mNickNameTv.setText(getString(R.string.fashi));
                    refreshView();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsTemple(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                if (!resp.contains("not")) {
                    XuperAccount.setAccountType(XuperAccount.ACCOUNT_TYPE_SIYUAN);
                    mNickNameTv.setText(getString(R.string.siyuan));
                    refreshView();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

//        mNickNameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //测试
//                SkipInsideUtil.skipInsideActivity(mContext, MasterListActivity.class);
//            }
//        });
        refreshView();
    }


    @OnClick({R.id.btn_master, R.id.btn_temple, R.id.btn_jjh, R.id.user_part1, R.id.user_part2, R.id.user_part3,
            R.id.btn_my_shanjv_1, R.id.btn_my_shanjv_2, R.id.btn_my_shanjv_3, R.id.btn_my_shanjv_4, R.id.btn_my_shanjv_5,
            R.id.tv_login, R.id.view_receive_coin, R.id.view_transfer_coin, R.id.tv_my_address, R.id.view_action_record
            , R.id.iv_setting})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (XuperAccount.ifLoginAccount()) {
                    DialogUtil.simpleDialog(mContext, "确认删除？", new DialogUtil.ConfirmCallBackInf() {
                        @Override
                        public void onConfirmClick(String content) {
                            XuperAccount.logoutAccount(mContext);
                        }
                    }, null);
                } else if (XuperAccount.ifHasAccount(mContext)) {
                    XuperAccount.checkAccount(mContext);
                } else {
                    SkipInsideUtil.skipInsideActivity(mContext, WalletGuideActivity.class);
                }
                break;
            case R.id.btn_my_shanjv_1:
                toMyShanjv(0);
                break;
            case R.id.btn_my_shanjv_2:
                toMyShanjv(1);
                break;
            case R.id.btn_my_shanjv_3:
                toMyShanjv(2);
                break;
            case R.id.btn_my_shanjv_4:
                toMyShanjv(3);
                break;
            case R.id.btn_my_shanjv_5:
                toMyShanjv(4);
                break;
            case R.id.user_part1:
                SkipInsideUtil.skipInsideActivity(mContext, MasterBackstageActivity.class);
                break;
            case R.id.user_part2:
                SkipInsideUtil.skipInsideActivity(mContext, TempleBackstageActivity.class);
                break;
            case R.id.user_part3:
                SkipInsideUtil.skipInsideActivity(mContext, JjhBackstageActivity.class);
                break;
            case R.id.btn_master:
                if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_FASHI) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {
                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengMasterActivity.class);
                }
                break;
            case R.id.btn_temple:
                if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_SIYUAN) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {

                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengTempleStep1Activity.class);
                }
                break;
            case R.id.btn_jjh:
                if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_JJH) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {

                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengJjhActivity.class);
                }
                break;
            case R.id.view_receive_coin:
                SkipInsideUtil.skipInsideActivity(mContext, ReceiveCoinActivity.class);
                break;
            case R.id.view_transfer_coin:
                SkipInsideUtil.skipInsideActivity(mContext, TransferCoinActivity.class);
                break;
            case R.id.tv_my_address:
                UIUtils.copyString(mContext, XuperAccount.getAddress());
                break;
            case R.id.view_action_record:
                SkipInsideUtil.skipInsideActivity(mContext, ActionRecordActivity.class);
                break;
            case R.id.iv_setting:
                SkipInsideUtil.skipInsideActivity(mContext, SettingActivity.class);
                break;
            default:
                break;

        }
    }

    private void toMyShanjv(int i) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("myshanjv", i);
        SkipInsideUtil.skipInsideActivity(mContext, MyShanjvActivity.class, hashMap);

    }

}
