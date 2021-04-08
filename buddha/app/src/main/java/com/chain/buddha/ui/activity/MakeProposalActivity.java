package com.chain.buddha.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.BaseActivity;
import com.chain.buddha.utils.DialogUtil;
import com.chain.buddha.utils.TimeUtils;
import com.chain.buddha.utils.ToastUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class MakeProposalActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;

    @BindView(R.id.et_new_value)
    EditText mNewValue;

    @BindView(R.id.tv_choose_date)
    TextView mDateTv;

    private String expire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_proposal);
        mTitle.setText("发起提案");

    }


    @OnClick(R.id.tv_submit)
    void submit() {
        String newValue = mNewValue.getText().toString();
        DialogUtil.simpleDialog(mContext, "确认提交？", new DialogUtil.ConfirmCallBackInf() {
            @Override
            public void onConfirmClick(String content) {
                XuperApi.makeProposal("test_proposal", newValue, expire, new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String resp) {
                        ToastUtils.show(mContext, "发起成功");
                    }

                    @Override
                    public void onFail(String message) {
                        DialogUtil.tipDialog(mContext, message);
                    }
                });
            }
        });
    }

    @OnClick(R.id.tv_choose_date)
    void chooseDate() {
        //获取实例，包含当前年月日
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = i + "-" + (i1 + 1) + "-" + i2;
                mDateTv.setText(date);
                expire = TimeUtils.getStringToDate(date) + "";
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MARCH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
