package io.agora.vlive.protocol;

import io.agora.vlive.protocol.model.request.AudienceListRequest;
import io.agora.vlive.protocol.model.request.CreateRoomRequest;
import io.agora.vlive.protocol.model.request.OssPolicyRequest;
import io.agora.vlive.protocol.model.request.ProductRequest;
import io.agora.vlive.protocol.model.request.RefreshTokenRequest;
import io.agora.vlive.protocol.model.request.Request;
import io.agora.vlive.protocol.model.request.RoomListRequest;
import io.agora.vlive.protocol.model.request.RoomRequest;
import io.agora.vlive.protocol.model.request.SeatInteractionRequest;
import io.agora.vlive.protocol.model.request.SendGiftRequest;

public class ClientProxy {
    public static final int ROOM_TYPE_ECOMMERCE = 5;

    private static final String APP_CODE = "ent-super";
    private static final int OS_TYPE = 2;

    // 1 means android phone app (rather than a pad app)
    private static final int TERMINAL_TYPE = 1;

    private Client mClient;
    private long mReqId = 1;

    private static volatile ClientProxy sInstance;

    private ClientProxy() {
        mClient = new Client();
    }

    public static ClientProxy instance() {
        if (sInstance == null) {
            synchronized (ClientProxy.class) {
                if (sInstance == null) {
                    sInstance = new ClientProxy();
                }
            }
        }

        return sInstance;
    }

    public long sendRequest(int request, Object params) {
        switch (request) {
            case Request.GIFT_LIST:
                mClient.requestGiftList(mReqId);
                break;
            case Request.MUSIC_LIST:
                mClient.requestMusicList(mReqId);
                break;
            case Request.OSS:
                OssPolicyRequest ossRequest = (OssPolicyRequest) params;
                mClient.requestOssPolicy(mReqId, ossRequest.token, ossRequest.type);
                break;

            case Request.ROOM_LIST:
                RoomListRequest roomListRequest = (RoomListRequest) params;
                mClient.requestRoomList(mReqId, roomListRequest.token, roomListRequest.nextId,
                        roomListRequest.count, roomListRequest.type, roomListRequest.pkState);
                break;
            case Request.CREATE_ROOM:
                CreateRoomRequest createRoomRequest = (CreateRoomRequest) params;
                mClient.createRoom(mReqId, createRoomRequest.token,
                        createRoomRequest.roomName, createRoomRequest.type,
                        createRoomRequest.avatar);
                break;
            case Request.ENTER_ROOM:
                RoomRequest roomRequest = (RoomRequest) params;
                mClient.enterRoom(mReqId, roomRequest.token, roomRequest.roomId);
                break;
            case Request.LEAVE_ROOM:
                roomRequest = (RoomRequest) params;
                mClient.leaveRoom(mReqId, roomRequest.token, roomRequest.roomId);
                break;
            case Request.AUDIENCE_LIST:
                AudienceListRequest audienceRequest = (AudienceListRequest) params;
                mClient.requestAudienceList(mReqId, audienceRequest.token, audienceRequest.roomId,
                        audienceRequest.nextId, audienceRequest.count, audienceRequest.type);
                break;
            case Request.SEND_GIFT:
                SendGiftRequest sendGiftRequest = (SendGiftRequest) params;
                mClient.sendGift(mReqId, sendGiftRequest.token, sendGiftRequest.roomId,
                        sendGiftRequest.giftId, sendGiftRequest.count);
                break;
            case Request.GIFT_RANK:
                String roomId = (String) params;
                mClient.giftRank(mReqId, roomId);
                break;
            case Request.SEAT_STATE:
                roomRequest = (RoomRequest) params;
                mClient.requestSeatState(mReqId, roomRequest.token, roomRequest.roomId);
                break;

            case Request.REFRESH_TOKEN:
                RefreshTokenRequest refreshTokenRequest = (RefreshTokenRequest) params;
                mClient.refreshToken(mReqId, refreshTokenRequest.token, refreshTokenRequest.roomId);
                break;

            case Request.PRODUCT_LIST:
                ProductRequest productRequest = (ProductRequest) params;
                mClient.requestProductList(productRequest.token, productRequest.roomId);
                break;
            case Request.PRODUCT_MANAGE:
                productRequest = (ProductRequest) params;
                mClient.requestManageProductState(productRequest.token,
                        productRequest.roomId, productRequest.productId,
                        productRequest.state);
                break;
            case Request.PRODUCT_PURCHASE:
                productRequest = (ProductRequest) params;
                mClient.requestPurchaseProduct(productRequest.token,
                        productRequest.roomId, productRequest.productId,
                        productRequest.count);
                break;
        }

        return mReqId++;
    }

    public void registerProxyListener(ClientProxyListener listener) {
        mClient.registerProxyListener(listener);
    }

    public void removeProxyListener(ClientProxyListener listener) {
        mClient.removeProxyListener(listener);
    }
}
