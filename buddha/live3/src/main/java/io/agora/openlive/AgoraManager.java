package io.agora.openlive;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.agora.openlive.rtc.AgoraEventHandler;
import io.agora.openlive.rtc.EngineConfig;
import io.agora.openlive.rtc.EventHandler;
import io.agora.openlive.stats.StatsManager;
import io.agora.openlive.utils.FileUtil;
import io.agora.openlive.utils.PrefManager;
import io.agora.rtc.RtcEngine;

public class AgoraManager {

    private static AgoraManager agoraManager;
    private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();
    private Context context;

    public static AgoraManager getInstance() {
        return agoraManager;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        agoraManager = new AgoraManager(context);
        agoraManager.onCreate();
    }


    /**
     * 销毁
     */
    public static void clean() {
        agoraManager.onTerminate();
    }

    private AgoraManager(Context context) {
        this.context = context;
    }

    public void onCreate() {
        try {
            mRtcEngine = RtcEngine.create(context, context.getString(R.string.private_app_id), mHandler);
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initConfig();
    }

    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(context);
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }


    public void onTerminate() {
        RtcEngine.destroy();
    }
}
