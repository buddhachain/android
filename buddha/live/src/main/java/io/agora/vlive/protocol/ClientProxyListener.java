package io.agora.vlive.protocol;

import io.agora.vlive.protocol.model.response.AudienceListResponse;
import io.agora.vlive.protocol.model.response.CreateRoomResponse;
import io.agora.vlive.protocol.model.response.EnterRoomResponse;
import io.agora.vlive.protocol.model.response.GiftListResponse;
import io.agora.vlive.protocol.model.response.GiftRankResponse;
import io.agora.vlive.protocol.model.response.LeaveRoomResponse;
import io.agora.vlive.protocol.model.response.ModifyUserStateResponse;
import io.agora.vlive.protocol.model.response.MusicListResponse;
import io.agora.vlive.protocol.model.response.OssPolicyResponse;
import io.agora.vlive.protocol.model.response.ProductListResponse;
import io.agora.vlive.protocol.model.response.RefreshTokenResponse;
import io.agora.vlive.protocol.model.response.RoomListResponse;
import io.agora.vlive.protocol.model.response.SeatStateResponse;
import io.agora.vlive.protocol.model.response.SendGiftResponse;

public interface ClientProxyListener {

    void onRefreshTokenResponse(RefreshTokenResponse refreshTokenResponse);

    void onOssPolicyResponse(OssPolicyResponse response);

    void onMusicLisResponse(MusicListResponse response);

    void onGiftListResponse(GiftListResponse response);

    void onRoomListResponse(RoomListResponse response);

    void onCreateRoomResponse(CreateRoomResponse response);

    void onEnterRoomResponse(EnterRoomResponse response);

    void onLeaveRoomResponse(LeaveRoomResponse response);

    void onAudienceListResponse(AudienceListResponse response);

    void onRequestSeatStateResponse(SeatStateResponse response);

    void onSendGiftResponse(SendGiftResponse response);

    void onGiftRankResponse(GiftRankResponse response);

    void onGetProductListResponse(ProductListResponse response);

    void onProductStateChangedResponse(String productId, int state, boolean success);

    void onProductPurchasedResponse(boolean success);

    void onResponseError(int requestType, int error, String message);
}