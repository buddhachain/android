package io.agora.vlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.PatternFlattener;
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter;
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter;
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
//import com.faceunity.FURenderer;
import com.tencent.bugly.crashreport.CrashReport;

import io.agora.capture.video.camera.CameraManager;
import io.agora.framework.PreprocessorFaceUnity;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.RtmClient;
import io.agora.vlive.agora.AgoraEngine;
import io.agora.vlive.agora.rtc.RtcEventHandler;
import io.agora.vlive.protocol.ClientProxy;
import io.agora.vlive.utils.Global;
import io.agora.vlive.utils.UserUtil;

public class AgoraLiveManager {
    private static final String TAG = AgoraLiveManager.class.getSimpleName();

    private static AgoraLiveManager agoraLiveManager;

    private SharedPreferences mPref;
    private Config mConfig;
    private AgoraEngine mAgoraEngine;
    private CameraManager mCameraVideoManager;
    private Context mContext;

    public static AgoraLiveManager getInstance() {
        return agoraLiveManager;
    }

    public String getAppVersion() {
        return "3.2.0";
    }

    public static Context getContext() {
        return agoraLiveManager.mContext;
    }

    public static void init(Context context) {
        agoraLiveManager = new AgoraLiveManager();
        agoraLiveManager.create(context);
    }

    public void create(Context context) {
        mContext = context;
        mPref = context.getSharedPreferences(Global.Constants.SF_NAME, Context.MODE_PRIVATE);
        mConfig = new Config(this);
        initXLog();
        initVideoGlobally();
        initCrashReport();
        XLog.i("onApplicationCreate");
    }

    public Config config() {
        return mConfig;
    }

    public SharedPreferences preferences() {
        return mPref;
    }

    public void initEngine(String appId) {
        mAgoraEngine = new AgoraEngine(this, appId);
    }

    public RtcEngine rtcEngine() {
        return mAgoraEngine != null ? mAgoraEngine.rtcEngine() : null;
    }

    public RtmClient rtmClient() {
        return mAgoraEngine != null ? mAgoraEngine.rtmClient() : null;
    }

    public ClientProxy proxy() {
        return ClientProxy.instance();
    }

    public void registerRtcHandler(RtcEventHandler handler) {
        mAgoraEngine.registerRtcHandler(handler);
    }

    public void removeRtcHandler(RtcEventHandler handler) {
        mAgoraEngine.removeRtcHandler(handler);
    }

    public CameraManager cameraVideoManager() {
        return mCameraVideoManager;
    }

    private void initVideoGlobally() {
        new Thread(() -> {
//            FURenderer.initFURenderer(mContext);
            PreprocessorFaceUnity preprocessor =
                    new PreprocessorFaceUnity(mContext);
            mCameraVideoManager = new CameraManager(
                    mContext, preprocessor);
            mCameraVideoManager.setCameraStateListener(preprocessor);
        }).start();
    }

    private void initXLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ?
                        LogLevel.DEBUG : LogLevel.INFO)                         // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                .tag("AgoraLive")                                               // Specify TAG, default: "X-LOG"
                //.t()                                                            // Enable thread info, disabled by default
                .st(Global.Constants.LOG_CLASS_DEPTH)                           // Enable stack trace info with depth 2, disabled by default
                // .b()                                                            // Enable border, disabled by default
                .jsonFormatter(new DefaultJsonFormatter())                      // Default: DefaultJsonFormatter
                .xmlFormatter(new DefaultXmlFormatter())                        // Default: DefaultXmlFormatter
                .throwableFormatter(new DefaultThrowableFormatter())            // Default: DefaultThrowableFormatter
                .threadFormatter(new DefaultThreadFormatter())                  // Default: DefaultThreadFormatter
                .stackTraceFormatter(new DefaultStackTraceFormatter())          // Default: DefaultStackTraceFormatter
                .build();

        Printer androidPrinter = new AndroidPrinter();                          // Printer that print the log using android.util.Log

        String flatPattern = "{d yy/MM/dd HH:mm:ss} {l}|{t}: {m}";
        Printer filePrinter = new FilePrinter                                   // Printer that print the log to the file system
                .Builder(UserUtil.appLogFolderPath(mContext))         // Specify the path to save log file
                .fileNameGenerator(new DateFileNameGenerator())                 // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(new FileSizeBackupStrategy(
                        Global.Constants.APP_LOG_SIZE))                         // Default: FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(new FileLastModifiedCleanStrategy(
                        Global.Constants.LOG_DURATION))
                .flattener(new PatternFlattener(flatPattern))                   // Default: DefaultFlattener
                .build();

        XLog.init(                                                              // Initialize XLog
                config,                                                         // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                androidPrinter,
                filePrinter);
    }

    private void initCrashReport() {
        String buglyAppId = mContext.getResources().getString(R.string.bugly_app_id);
        if (TextUtils.isEmpty(buglyAppId)) {
            XLog.i("Bugly app id not found, crash report initialize skipped");
        } else {
            CrashReport.initCrashReport(mContext,
                    buglyAppId, BuildConfig.DEBUG);
        }
    }

    public void destroy() {
        XLog.i("onApplicationTerminate");

        mAgoraEngine.release();
    }
}