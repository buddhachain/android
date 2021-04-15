package io.agora.openlive.utils;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Map;

import io.agora.openlive.R;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;

public class RTMUtils {
    private static final String TAG = "RTMUtils";
    private static RtmClient mRtmClient;

    public static RtmClient getRtmClient() {
        return mRtmClient;
    }


    public static void initClient(Context context) {
        try {
            mRtmClient = RtmClient.createInstance(context, "de516c63b96b41ecbdd41935cab5ccd8",
                    new RtmClientListener() {
                        @Override
                        public void onConnectionStateChanged(int state, int reason) {
                            Log.d(TAG, "Connection state changes to "
                                    + state + " reason: " + reason);
                        }

                        @Override
                        public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
                            String msg = rtmMessage.getText();
                            Log.d(TAG, "Message received " + " from " + peerId + msg
                            );
                        }

                        @Override
                        public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

                        }

                        @Override
                        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

                        }

                        @Override
                        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                        }

                        @Override
                        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                        }

                        @Override
                        public void onTokenExpired() {

                        }

                        @Override
                        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login() {
        mRtmClient.login(null, "aaa", new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.d(TAG, "login success!");
                createChannel();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(TAG, "login failure!");
            }
        });
    }

    private static RtmChannel mRtmChannel;

    public static void createChannel() {
        mRtmChannel = mRtmClient.createChannel("demoChannelId", new RtmChannelListener() {
            @Override
            public void onMemberCountUpdated(int i) {

            }

            @Override
            public void onAttributesUpdated(List<RtmChannelAttribute> list) {

            }

            @Override
            public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
                String raw = new String(rtmMessage.getRawMessage());
                Log.e(TAG, rtmMessage.getText());
            }

            @Override
            public void onImageMessageReceived(RtmImageMessage rtmImageMessage, RtmChannelMember rtmChannelMember) {

            }

            @Override
            public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

            }

            @Override
            public void onMemberJoined(RtmChannelMember rtmChannelMember) {

            }

            @Override
            public void onMemberLeft(RtmChannelMember rtmChannelMember) {

            }
        });
        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.d(TAG, "Successfully joins the channel!");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(TAG, "join channel failure! errorCode = "
                        + errorInfo.getErrorCode());
            }
        });
    }

    public static void sendChannelMsg() {
        RtmMessage message = mRtmClient.createMessage();
        message.setRawMessage("haha".getBytes());
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "ok");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, errorInfo.toString());
            }
        });
    }
}
