package com.chain.buddha.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.adapter.MasterListAdapter;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.SkipInsideUtil;
import com.chain.buddha.utils.StringUtils;
import com.chain.buddha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 寺院信息
 */
public class TempleInfoActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.tv_apply)
    TextView mApplyTv;

    /**
     * 寺院内法师列表
     */
    @BindView(R.id.rv_temple)
    RecyclerView mMasterRv;
    private List<String> mMasterList;
    private MasterListAdapter mMasterAdapter;

    private String templeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_info);
        mTitle.setText("寺院信息");
        templeId = getIntent().getStringExtra(SkipInsideUtil.SKIP_KEY_ID);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        mMasterRv.setLayoutManager(layoutManager1);
        mMasterList = new ArrayList<>();
        mMasterAdapter = new MasterListAdapter(mContext, mMasterList);
        mMasterRv.setAdapter(mMasterAdapter);
        mMasterAdapter.addChildClickViewIds(R.id.tv_delete, R.id.tv_update);
        mMasterAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String[] list = mMasterList.get(position).split(",");
                String masterid = list[1];
                String templeid = list[0];
                if (StringUtils.equals(list[2], "1")) {
                    return;
                }
                DialogUtil.simpleDialog(mContext, "确认批准入驻？", new DialogUtil.ConfirmCallBackObject<String>() {
                    @Override
                    public void onConfirmClick(String content) {
                        XuperApi.approveJoinTemple(templeid, masterid, new ResponseCallBack<String>() {
                            @Override
                            public void onSuccess(String resp) {
                                ToastUtils.show(mContext, "批准成功");
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

        });

        if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_FASHI) {
            mApplyTv.setVisibility(View.VISIBLE);
        } else {
            mApplyTv.setVisibility(View.GONE);
        }
        getData();

    }

    /**
     * 获取入驻法师列表
     */
    void getData() {
        XuperApi.templeMasterList(templeId, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String resp) {
                Log.e("resp", resp);
                try {
                    resp = resp.replaceAll("\\}", "");
                    String[] list = resp.split("\\{");
                    mMasterList.clear();
                    mMasterList.addAll(Arrays.asList(list));
                    mMasterList.remove(0);
                    mMasterAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                Log.e("message", message);
            }
        });

        if (XuperAccount.getAccountType() == XuperAccount.ACCOUNT_TYPE_FASHI) {
            XuperApi.is_in_temple(templeId, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String resp) {
                    Log.e("resp", resp);
                    if (resp.contains("not")) {
                        mApplyTv.setText("申请入驻");
                        mApplyTv.setBackgroundResource(R.color.base_color);
                        mApplyTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
                    } else {
                        mApplyTv.setText("已入驻");
                        mApplyTv.setBackgroundResource(R.color.transparent);
                        mApplyTv.setTextColor(ContextCompat.getColor(mContext, R.color.base_color));
                    }
                    try {
                        mApplyTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogUtil.simpleDialog(mContext, "确认入驻该寺院？", new DialogUtil.ConfirmCallBackObject<String>() {
                                    @Override
                                    public void onConfirmClick(String content) {
                                        XuperApi.applyJoinTemple(templeId, new ResponseCallBack() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                ToastUtils.show(mContext, "申请成功，请等待审核");
                                            }

                                            @Override
                                            public void onFail(String message) {
                                                DialogUtil.tipDialog(mContext, message);
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String message) {
                    Log.e("message", message);
                }
            });
        }
    }

}
