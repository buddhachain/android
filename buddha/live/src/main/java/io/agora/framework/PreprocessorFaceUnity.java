package io.agora.framework;

import android.content.Context;
import android.opengl.GLES20;

import io.agora.capture.video.camera.CameraVideoChannel;
import io.agora.capture.video.camera.VideoCaptureFrame;
import io.agora.framework.modules.channels.VideoChannel;
import io.agora.framework.modules.processors.IPreprocessor;

public class PreprocessorFaceUnity implements IPreprocessor, CameraVideoChannel.OnCameraStateListener {
    public interface OnFirstFrameListener {
        void onFirstFrame();
    }

    public interface OnFuEffectBundleLoadedListener {
        void onFuEffectBundleLoaded();
    }

    public final static int MSG_EFFECT_BUNDLE_COMPLETE = 1;

    private final static String TAG = PreprocessorFaceUnity.class.getSimpleName();
    private final static int ANIMOJI_COUNT = 2;

    public static final float DEFAULT_BLUR_VALUE = 0.7f;
    public static final float DEFAULT_WHITEN_VALUE = 0.3f;
    public static final float DEFAULT_CHEEK_VALUE = 0f;
    public static final float DEFAULT_EYE_VALUE = 0.4f;

    private Context mContext;
    private boolean mEnabled;
    private boolean mAuthenticated = true;


    private int mEffectBackgroundHandle;
    private int mEffectAnimojiHaskiHandle;
    private int mEffectAnimojiGirlHandle;
    private int mBeautyHandle;

    private OnFuEffectBundleLoadedListener mBundleListener;
    private OnFirstFrameListener mFirstFrameListener;

    public PreprocessorFaceUnity(Context context) {
        mContext = context;
    }

    @Override
    public VideoCaptureFrame onPreProcessFrame(VideoCaptureFrame outFrame, VideoChannel.ChannelContext context) {

        return outFrame;
    }

    @Override
    public void initPreprocessor() {

    }

    public boolean FUAuthenticated() {
        return mAuthenticated;
    }

    private void initAnimoji() {

    }

    public void setOnBundleLoadedListener(OnFuEffectBundleLoadedListener listener) {
        mBundleListener = listener;
    }

    public void setOnFirstFrameListener(OnFirstFrameListener listener) {
        mFirstFrameListener = listener;
    }

    private void enableBeauty() {

    }

    public void onAnimojiSelected(int index) {

    }

    @Override
    public void enablePreProcess(boolean enabled) {
        if (mAuthenticated) mEnabled = enabled;
    }

    @Override
    public void releasePreprocessor(VideoChannel.ChannelContext context) {

    }

    @Override
    public void setBlurValue(float blur) {

    }

    @Override
    public void setWhitenValue(float whiten) {

    }

    @Override
    public void setCheekValue(float cheek) {

    }

    @Override
    public void setEyeValue(float eye) {

    }

    @Override
    public void onFrameFrame() {
        if (mFirstFrameListener != null) {
            mFirstFrameListener.onFirstFrame();
        }
    }
}
