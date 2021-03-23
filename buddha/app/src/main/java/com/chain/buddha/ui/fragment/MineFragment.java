package com.chain.buddha.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.baidu.xuper.api.Transaction;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.TestAccount;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.activity.JjhBackstageActivity;
import com.chain.buddha.ui.activity.LoginActivity;
import com.chain.buddha.ui.activity.MasterBackstageActivity;
import com.chain.buddha.ui.activity.MasterListActivity;
import com.chain.buddha.ui.activity.MyShanjvActivity;
import com.chain.buddha.ui.activity.RenzhengJjhActivity;
import com.chain.buddha.ui.activity.RenzhengMasterActivity;
import com.chain.buddha.ui.activity.RenzhengTempleActivity;
import com.chain.buddha.ui.activity.SendShanjvActivity;
import com.chain.buddha.ui.activity.TempleBackstageActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.EventBeans;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyInit() {
        requestData();
//        IpfsUtils.test(mContext);
    }

    void requestData() {
        if (XuperAccount.ifLoginAccount()) {
            mNickNameTv.setText(getString(R.string.normal));
            mLoginTv.setText(getString(R.string.logout));
        } else if (XuperAccount.ifHasAccount()) {
            mNickNameTv.setText(getString(R.string.normal));
            mLoginTv.setText(getString(R.string.open_wallet));
        } else {
            mNickNameTv.setText(getString(R.string.not_login));
            mLoginTv.setText(getString(R.string.login));
        }
        mMyAddressTv.setText(getString(R.string.account_address) + XuperAccount.getAddress());

        XuperApi.getBalance(XuperAccount.getAddress(), new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mMyBalanceTv.setText(getString(R.string.account_balance) + s);
            }

            @Override
            public void onFail(String message) {
                ToastUtils.show(mContext, message);
            }
        });

        XuperApi.getAccountByAK(XuperAccount.getAddress(), new ResponseCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
//                mNickNameTv.setText(strings.toString());
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
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

//        XuperApi.requestMasterList(new ResponseCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//            }
//
//            @Override
//            public void onFail(String message) {
//            }
//        });
//        mNickNameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //测试
//                SkipInsideUtil.skipInsideActivity(mContext, MasterListActivity.class);
//            }
//        });
    }


    @OnClick({R.id.btn_master, R.id.btn_temple, R.id.btn_jjh, R.id.user_part1, R.id.user_part2, R.id.user_part3,
            R.id.btn_my_shanjv_1, R.id.btn_my_shanjv_2, R.id.btn_my_shanjv_3, R.id.btn_my_shanjv_4, R.id.btn_my_shanjv_5, R.id.tv_login})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (XuperAccount.ifLoginAccount()) {
                    DialogUtil.simpleDialog(mContext, "确认退出？", new DialogUtil.ConfirmCallBackInf() {
                        @Override
                        public void onConfirmClick(String content) {
                            XuperAccount.logoutAccount();
                        }
                    }, null);
                } else if (XuperAccount.ifHasAccount()) {
                    XuperAccount.checkAccount(mContext);
                } else {
                    SkipInsideUtil.skipInsideActivity(mContext, LoginActivity.class);
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
            case R.id.user_part2:
            case R.id.user_part3:
                toBackStage((TextView) view);
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

                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengTempleActivity.class);
                }
                break;
            case R.id.btn_jjh:
                if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_JJH) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {

                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengJjhActivity.class);
                }
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

    private void toBackStage(TextView view) {
        String s = view.getText().toString();
        switch (s) {
            case "法师":
                SkipInsideUtil.skipInsideActivity(mContext, MasterBackstageActivity.class);
                break;
            case "寺院":
                SkipInsideUtil.skipInsideActivity(mContext, TempleBackstageActivity.class);
                break;
            case "基金会":
                SkipInsideUtil.skipInsideActivity(mContext, JjhBackstageActivity.class);
                break;
            default:
                break;
        }

    }
}
