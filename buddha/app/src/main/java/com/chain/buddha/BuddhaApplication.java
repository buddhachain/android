package com.chain.buddha;

import android.app.Application;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class BuddhaApplication extends Application {
    private static BuddhaApplication instance;


    //    实例化一次
    public synchronized static BuddhaApplication getInstance(){return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

    }
}
