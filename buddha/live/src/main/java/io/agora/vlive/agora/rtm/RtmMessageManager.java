package io.agora.vlive.agora.rtm;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;
import io.agora.vlive.agora.rtm.model.ChatMessage;
import io.agora.vlive.agora.rtm.model.GiftMessage;
import io.agora.vlive.agora.rtm.model.GiftRankMessage;
import io.agora.vlive.agora.rtm.model.NotificationMessage;
import io.agora.vlive.agora.rtm.model.OwnerStateMessage;
import io.agora.vlive.agora.rtm.model.ProductStatedChangedMessage;
import io.agora.vlive.agora.rtm.model.SeatStateMessage;

public class RtmMessageManager implements RtmClientListener, RtmChannelListener {
    private static final String TAG = RtmMessageManager.class.getSimpleName();

    private static final int PEER_MSG_TYPE_SEAT = 1;
    private static final int PEER_MSG_TYPE_PK = 2;
    private static final int PEER_MSG_TYPE_OWNER_PK_NOTIFY = 3;

    public static final int CHANNEL_MSG_TYPE_CHAT = 1;

    // Users enter or leave the room
    private static final int CHANNEL_MSG_TYPE_NOTIFY = 2;

    // Where the UI needs to show the user rank of gift values
    private static final int CHANNEL_MSG_TYPE_GIFT_RANK = 3;

    // Notifies that the room owner has changed his state
    private static final int CHANNEL_MSG_CMD_OWNER_STATE = 4;

    private static final int CHANNEL_MSG_TYPE_GIFT = 7;

    private static final int CHANNEL_MSG_TYPE_LEAVE = 8;

    private static final int CHANNEL_MSG_TYPE_PRODUCT_STATE_PURCHASED = 9;

    private static final int CHANNEL_MSG_TYPE_PRODUCT_STATE_CHANGED = 10;

    private volatile static RtmMessageManager sInstance;

    private RtmClient mRtmClient;
    private RtmChannel mRtmChannel;
    private SendMessageOptions mOptions;
    private List<RtmMessageListener> mMessageListeners;
    private Handler mHandler;

    private RtmMessageManager() {
        mOptions = new SendMessageOptions();
        mOptions.enableOfflineMessaging = false;
        mOptions.enableHistoricalMessaging = false;
        mMessageListeners = new ArrayList<>();
    }

    public static RtmMessageManager instance() {
        if (sInstance == null) {
            synchronized (RtmMessageManager.class) {
                if (sInstance == null) {
                    sInstance = new RtmMessageManager();
                }
            }
        }
        return sInstance;
    }

    public void init(RtmClient client) {
        mRtmClient = client;
    }

    public synchronized void joinChannel(String channel, ResultCallback<Void> callback) {
        if (mRtmChannel != null || mRtmClient == null) {
            return;
        }

        mRtmChannel = mRtmClient.createChannel(channel, this);
        mRtmChannel.join(callback);
    }

    public synchronized void leaveChannel(ResultCallback<Void> callback) {
        if (mRtmChannel == null) return;
        mRtmChannel.leave(callback);
        mRtmChannel.release();
        mRtmChannel = null;
    }

    private void sendChannelMessage(String message, ResultCallback<Void> callback) {
        if (mRtmChannel == null) return;
        RtmMessage msg = mRtmClient.createMessage(message);
        mRtmChannel.sendMessage(msg, mOptions, callback);
    }

    public void sendChatMessage(String userId, String nickname, String content, ResultCallback<Void> callback) {
        String json = getChatMessageJsonString(userId, nickname, content);
        sendChannelMessage(json, callback);
    }

    private String getChatMessageJsonString(String userId, String nickname, String content) {
        ChatMessage data = new ChatMessage(userId, nickname, content);
        return new GsonBuilder().create().toJson(data);
    }

    public void registerMessageHandler(RtmMessageListener handler) {
        if (!mMessageListeners.contains(handler)) mMessageListeners.add(handler);
    }

    public void removeMessageHandler(RtmMessageListener handler) {
        mMessageListeners.remove(handler);
    }

    public void setCallbackThread(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onConnectionStateChanged(int state, int reason) {
        for (RtmMessageListener handler : mMessageListeners) {
            handler.onRtmConnectionStateChanged(state, reason);
        }
    }

    @Override
    public void onMessageReceived(RtmMessage rtmMessage, final String peerId) {
        String rtmMessageString = rtmMessage.getText();
        XLog.d("peer message: " + rtmMessageString);

        try {
            JSONObject obj = new JSONObject(rtmMessageString);
            int cmd = obj.getInt("cmd");
            switch (cmd) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTokenExpired() {
        for (RtmMessageListener listener : mMessageListeners) {
            listener.onRtmTokenExpired();
        }
    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

    }

    @Override
    public void onMemberCountUpdated(int memberCount) {

    }

    @Override
    public void onAttributesUpdated(List<RtmChannelAttribute> attributeList) {
        for (RtmMessageListener listener : mMessageListeners) {
            listener.onRtmAttributesUpdated(attributeList);
        }
    }

    @Override
    public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember fromMember) {
        // Where channel messages are received
        String json = rtmMessage.getText();
        XLog.d("Channel message: " + rtmMessage.getText());

        Gson gson = new Gson();
        int cmd = -1;
        try {
            JSONObject obj = new JSONObject(json);
            cmd = obj.getInt("cmd");

            for (final RtmMessageListener listener : mMessageListeners) {
                switch (cmd) {
                    case CHANNEL_MSG_TYPE_CHAT:
                        ChatMessage chatMessage = gson.fromJson(json, ChatMessage.class);
                        handleChatMessage(listener, chatMessage);
                        break;
                    case CHANNEL_MSG_TYPE_NOTIFY:
                        NotificationMessage notification = gson.fromJson(json, NotificationMessage.class);
                        handleNotificationMessage(listener, notification);
                        break;
                    case CHANNEL_MSG_TYPE_GIFT_RANK:
                        GiftRankMessage rankMessage = gson.fromJson(json, GiftRankMessage.class);
                        handleGiftRankMessage(listener, rankMessage);
                        break;
                    case CHANNEL_MSG_CMD_OWNER_STATE:
                        OwnerStateMessage ownerMessage = gson.fromJson(json, OwnerStateMessage.class);
                        handleOwnerStateMessage(listener, ownerMessage);
                        break;
                    case CHANNEL_MSG_TYPE_GIFT:
                        GiftMessage giftMessage = gson.fromJson(json, GiftMessage.class);
                        handleGiftMessage(listener, giftMessage);
                        break;
                    case CHANNEL_MSG_TYPE_LEAVE:
                        handleLeaveMessage(listener);
                        break;
                    case CHANNEL_MSG_TYPE_PRODUCT_STATE_CHANGED:
                        ProductStatedChangedMessage productStateChangedMessage =
                                gson.fromJson(json, ProductStatedChangedMessage.class);
                        handleProductStateChangedMessage(listener, productStateChangedMessage);
                        break;
                    case CHANNEL_MSG_TYPE_PRODUCT_STATE_PURCHASED:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleChatMessage(@NonNull RtmMessageListener listener, ChatMessage message) {
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmChannelMessageReceived(
                    message.data.fromUserId, message.data.fromUserName, message.data.message));
        } else {
            listener.onRtmChannelMessageReceived(message.data.fromUserId, message.data.fromUserName, message.data.message);
        }
    }

    private void handleNotificationMessage(@NonNull RtmMessageListener listener, NotificationMessage message) {
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmChannelNotification(message.data.total, message.data.list));
        } else {
            listener.onRtmChannelNotification(message.data.total, message.data.list);
        }
    }

    private void handleGiftRankMessage(@NonNull RtmMessageListener listener, GiftRankMessage message) {
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmRoomGiftRankChanged(message.data.total, message.data.list));
        } else {
            listener.onRtmRoomGiftRankChanged(message.data.total, message.data.list);
        }
    }

    private void handleOwnerStateMessage(@NonNull RtmMessageListener listener, OwnerStateMessage message) {
        OwnerStateMessage.OwnerState data = message.data;
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmOwnerStateChanged(data.userId, data.userName, data.uid, data.enableAudio, data.enableVideo));
        } else {
            listener.onRtmOwnerStateChanged(data.userId, data.userName, data.uid, data.enableAudio, data.enableVideo);
        }
    }


    private void handleGiftMessage(@NonNull RtmMessageListener listener, GiftMessage message) {
        GiftMessage.GiftMessageData data = message.data;
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmGiftMessage(data.fromUserId, data.fromUserName, data.toUserId, data.toUserName, data.giftId));
        } else {
            listener.onRtmGiftMessage(data.fromUserId, data.fromUserName, data.toUserId, data.toUserName, data.giftId);
        }
    }

    private void handleLeaveMessage(@NonNull RtmMessageListener listener) {
        if (mHandler != null) {
            mHandler.post(listener::onRtmLeaveMessage);
        } else {
            listener.onRtmLeaveMessage();
        }
    }

    private void handleProductStateChangedMessage(@NonNull RtmMessageListener listener,
                                                  ProductStatedChangedMessage message) {
        if (mHandler != null) {
            mHandler.post(() -> listener.onRtmProductStateChanged(message.data.productId, message.data.state));
        } else {
            listener.onRtmProductStateChanged(message.data.productId, message.data.state);
        }
    }

    @Override
    public void onMemberJoined(RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberLeft(RtmChannelMember rtmChannelMember) {

    }
}
