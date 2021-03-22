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
import com.chain.buddha.ui.activity.MasterBackstageActivity;
import com.chain.buddha.ui.activity.MasterListActivity;
import com.chain.buddha.ui.activity.MyShanjvActivity;
import com.chain.buddha.ui.activity.RenzhengJjhActivity;
import com.chain.buddha.ui.activity.RenzhengMasterActivity;
import com.chain.buddha.ui.activity.RenzhengTempleActivity;
import com.chain.buddha.ui.activity.SendShanjvActivity;
import com.chain.buddha.ui.activity.TempleBackstageActivity;
import com.chain.buddha.utils.IpfsUtils;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    boolean isMaster = false;
    boolean isTemple = false;
    boolean isJjh = false;

    @BindView(R.id.tv_my_address)
    TextView mMyAddressTv;

    @BindView(R.id.tv_my_balance)
    TextView mMyBalanceTv;

    @BindView(R.id.user_name)
    TextView mNickNameTv;


    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        mMyAddressTv.setText(getString(R.string.account_address) + XuperAccount.getAddress());
    }

    @Override
    protected void lazyInit() {
        requestData();
//        IpfsUtils.test(mContext);
    }

    void requestData() {
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
                mNickNameTv.setText(strings.toString());
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsFounder(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                mNickNameTv.append(resp);
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsMaster(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                mNickNameTv.append(resp);
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.getIsTemple(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                mNickNameTv.append(resp);
            }

            @Override
            public void onFail(String message) {

            }
        });

        XuperApi.requestMasterList(new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtils.show(mContext, s);
            }

            @Override
            public void onFail(String message) {
                ToastUtils.show(mContext, message);
            }
        });
        mNickNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //测试
                SkipInsideUtil.skipInsideActivity(mContext, MasterListActivity.class);
            }
        });
    }


    @OnClick({R.id.btn_master, R.id.btn_temple, R.id.btn_jjh, R.id.user_part1, R.id.user_part2, R.id.user_part3,
            R.id.btn_my_shanjv_1, R.id.btn_my_shanjv_2, R.id.btn_my_shanjv_3, R.id.btn_my_shanjv_4, R.id.btn_my_shanjv_5})
    void onClick(View view) {
        switch (view.getId()) {
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
                if (isMaster) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {
                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengMasterActivity.class);
                }
                break;
            case R.id.btn_temple:
                if (isTemple) {
                    SkipInsideUtil.skipInsideActivity(mContext, SendShanjvActivity.class);
                } else {

                    SkipInsideUtil.skipInsideActivity(mContext, RenzhengTempleActivity.class);
                }
                break;
            case R.id.btn_jjh:
                if (isJjh) {
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
