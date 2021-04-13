package com.chain.buddha;

import android.app.Activity;
import android.app.Application;

import androidx.multidex.MultiDex;

import com.chain.buddha.Xuper.XuperAccount;
import com.mob.MobSDK;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import io.agora.openlive.AgoraManager;

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
        MultiDex.install(instance);
        ZXingLibrary.initDisplayOpinion(this);
        MobSDK.submitPolicyGrantResult(true, null);
        AgoraManager.init(instance);
        XuperAccount.initAccount();
    }

    public static Activity getCurrencyActivity() {
        return mCurrencyActivity;
    }

    public static void setCurrencyActivity(Activity mCurrencyActivity) {
        BuddhaApplication.mCurrencyActivity = mCurrencyActivity;
    }
}
