package com.chain.buddha;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

//import androidx.multidex.MultiDex;

import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.ui.activity.MainActivity;
import com.chain.buddha.utils.CustomCrashHandler;
import com.mob.MobSDK;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import io.agora.vlive.AgoraLiveManager;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class BuddhaApplication extends Application {
    private static BuddhaApplication instance;

    private static Activity mCurrencyActivity;

    //    实例化一次
    public synchronized static BuddhaApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

//        MultiDex.install(instance);
        initCrashCatch();
        ZXingLibrary.initDisplayOpinion(this);
        MobSDK.submitPolicyGrantResult(true, null);
        AgoraLiveManager.init(instance);
        XuperAccount.initAccount();
    }

    /**
     * creash处理
     */
    private void initCrashCatch() {
        //crash报告
        CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
    }

    public static Activity getCurrencyActivity() {
        return mCurrencyActivity;
    }

    public static void setCurrencyActivity(Activity mCurrencyActivity) {
        BuddhaApplication.mCurrencyActivity = mCurrencyActivity;
    }

    public void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
