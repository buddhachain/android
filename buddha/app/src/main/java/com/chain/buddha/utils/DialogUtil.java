package com.chain.buddha.utils;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.xuper.api.Account;
import com.chain.buddha.R;
import com.chain.buddha.Xuper.XuperAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 弹框工具类
 */
public class DialogUtil {
    public static final float DEFAULT_DIALOG_DIMAMOUNT = 0.6f;// 结果

    public static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static Context mContext;

    /**
     * dialog中点击确认按钮的回调
     *
     * @author heshuai
     */
    public interface ConfirmCallBackInf {
        void onConfirmClick(String content);
    }

    public interface ConfirmCallBackObject<T> {
        void onConfirmClick(T t);
    }

    /**
     * dialog中点击取消按钮的回调
     *
     * @author heshuai
     */
    public interface CancelCallBackInf {
        void onCancelClick(String content);
    }

    /**
     * dialog中点击不再提醒按钮的回调
     *
     * @author zengyuxin
     */
    public interface RemindCallBackInf {
        void onRemindClick(String content);
    }

    public static Dialog checkPswDialog(final Context context, final ConfirmCallBackInf callBack, final CancelCallBackInf cancelCallBack) {
        return checkPswDialog(context, "", callBack, cancelCallBack);
    }

    /**
     * 输入密码对话框
     *
     * @param context
     * @param callBack
     * @return
     */
    public static Dialog checkPswDialog(final Context context, String title, final ConfirmCallBackInf callBack, final CancelCallBackInf cancelCallBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.dialog_input_password, null,
                false);
        final Dialog dialog = new Dialog(context, R.style.ActionToastDialogStyle);
        mContext = context;
        dialog.setContentView(layout);

        if (!StringUtils.equalsNull(title)) {
            TextView mTitleTv = (TextView) layout.findViewById(R.id.dialog_msg_tv);
            mTitleTv.setText(title);
        }

        final TextView tv_error = layout.findViewById(R.id.tv_error);
        final EditText mEditText = layout.findViewById(R.id.dialog_edit_et);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_error.setText("");
            }
        });

        final TextView confirmBt = (TextView) layout.findViewById(R.id.btn_commit);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psw = mEditText.getText().toString();
                try {
                    Account account = Account.getAccountFromFile(XuperAccount.getAccountCachePath(context), psw);
                    XuperAccount.setAccount(account);
                    if (callBack != null) {
                        callBack.onConfirmClick("");
                    }
                    UIUtils.closeKeybord(mEditText, context);
                    dialog.dismiss();
                } catch (Throwable e) {
                    tv_error.setText("密码错误,请重试");
                }

            }
        });

        final TextView cancelBt = (TextView) layout.findViewById(R.id.btn_cancel);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.closeKeybord(mEditText, context);
                dialog.dismiss();
                if (cancelCallBack != null) {
                    cancelCallBack.onCancelClick("");
                }
            }
        });

        final boolean[] isShow = {false};
        final ImageView iv_hide = (ImageView) layout.findViewById(R.id.iv_hide);
        iv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow[0] = !isShow[0];
                if (isShow[0]) {
                    iv_hide.setImageResource(R.mipmap.icon_show);
                    mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    iv_hide.setImageResource(R.mipmap.icon_hide);
                    mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);

//        lp.width = context.getResources().getDimensionPixelSize(R.dimen.edit_dialog_width);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = DEFAULT_DIALOG_DIMAMOUNT;

        dialogWindow.setAttributes(lp);

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        try {
            dialog.show();
        } catch (Throwable t) {

        }
        return dialog;
    }

    public static Dialog simpleDialog(final Context context, String title, final ConfirmCallBackInf callBack) {
        return simpleDialog(context, title, callBack, null);
    }

    /**
     * 普通对话框 确认 取消
     *
     * @param context
     * @param callBack
     * @return
     */
    public static Dialog simpleDialog(final Context context, String title, final ConfirmCallBackInf callBack, final CancelCallBackInf cancelCallBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.dialog_simple, null,
                false);
        final Dialog dialog = new Dialog(context, R.style.ActionToastDialogStyle);
        mContext = context;
        dialog.setContentView(layout);

        if (!StringUtils.equalsNull(title)) {
            TextView mTitleTv = (TextView) layout.findViewById(R.id.dialog_msg_tv);
            mTitleTv.setText(title);
        }

        final TextView confirmBt = (TextView) layout.findViewById(R.id.btn_commit);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onConfirmClick("");
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        final TextView cancelBt = (TextView) layout.findViewById(R.id.btn_cancel);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (cancelCallBack != null) {
                    cancelCallBack.onCancelClick("");
                }
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);

//        lp.width = context.getResources().getDimensionPixelSize(R.dimen.edit_dialog_width);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = DEFAULT_DIALOG_DIMAMOUNT;

        dialogWindow.setAttributes(lp);

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        try {
            dialog.show();
        } catch (Throwable t) {

        }
        return dialog;
    }

    public static Dialog tipDialog(Context context, String msg) {
        return tipDialog(context, msg, true);
    }

    /**
     * 有一个按钮仅提示的对话框
     *
     * @param context
     * @param msg     提示信息
     * @return
     */
    public static Dialog tipDialog(Context context, String msg, boolean ifCanCancel) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_tip, null,
                false);
        final Dialog dialog = new Dialog(context, R.style.ActionToastDialogStyle);
        mContext = context;
        dialog.setContentView(layout);
        TextView mMsgTv = (TextView) layout.findViewById(R.id.dialog_msg_tv);
        if (!StringUtils.equalsNull(msg)) {
            mMsgTv.setText(msg);
        }

        final TextView okBt = (TextView) layout.findViewById(R.id.btn_commit);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(ifCanCancel);

        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);

        lp.width = context.getResources().getDimensionPixelSize(R.dimen.simple_dialog_width);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = DEFAULT_DIALOG_DIMAMOUNT;

        dialogWindow.setAttributes(lp);

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        try {
            dialog.show();
        } catch (Throwable t) {

        }
        return dialog;
    }

    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        loadingDialog.show();

        return loadingDialog;
    }

    /**
     * 关闭dialog
     *
     * http://blog.csdn.net/qq_21376985
     *
     * @param mDialogUtils
     */
    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
}

