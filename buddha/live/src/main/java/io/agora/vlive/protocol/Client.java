package io.agora.vlive.protocol;

import com.elvishew.xlog.XLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.agora.vlive.BuildConfig;
import io.agora.vlive.protocol.interfaces.GeneralService;
import io.agora.vlive.protocol.interfaces.ProductService;
import io.agora.vlive.protocol.interfaces.RoomService;
import io.agora.vlive.protocol.interfaces.UserService;
import io.agora.vlive.protocol.model.body.CreateRoomRequestBody;
import io.agora.vlive.protocol.model.body.PurchaseProductBody;
import io.agora.vlive.protocol.model.body.SendGiftBody;
import io.agora.vlive.protocol.model.request.Request;
import io.agora.vlive.protocol.model.response.AudienceListResponse;
import io.agora.vlive.protocol.model.response.BooleanResponse;
import io.agora.vlive.protocol.model.response.CreateRoomResponse;
import io.agora.vlive.protocol.model.response.EnterRoomResponse;
import io.agora.vlive.protocol.model.response.GiftListResponse;
import io.agora.vlive.protocol.model.response.GiftRankResponse;
import io.agora.vlive.protocol.model.response.LeaveRoomResponse;
import io.agora.vlive.protocol.model.response.MusicListResponse;
import io.agora.vlive.protocol.model.response.OssPolicyResponse;
import io.agora.vlive.protocol.model.response.ProductListResponse;
import io.agora.vlive.protocol.model.response.RefreshTokenResponse;
import io.agora.vlive.protocol.model.response.RoomListResponse;
import io.agora.vlive.protocol.model.response.SeatStateResponse;
import io.agora.vlive.protocol.model.response.SendGiftResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

class Client {
    private static final String PRODUCT_URL = "https://api-solutions.sh.agoralab.co";
    private static final String MSG_NULL_RESPONSE = "Response content is null";
    private static final int MAX_RESPONSE_THREAD = 10;
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 30;

    private static final int ERROR_OK = 0;
    private static final int ERROR_CONNECTION = -1;
    private static final int ERROR_NULL = -2;

    private GeneralService mGeneralService;
    private RoomService mRoomService;
    private UserService mUserService;
    private ProductService mProductService;

    private List<ClientProxyListener> mProxyListeners = new ArrayList<>();

    Client() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PRODUCT_URL)
                .client(okHttpClient)
                .callbackExecutor(Executors.newFixedThreadPool(MAX_RESPONSE_THREAD))
                .addConverterFactory(GsonConverterFactory.create());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(XLog::d);
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }

        Retrofit retrofit = builder.build();
        mGeneralService = retrofit.create(GeneralService.class);
        mRoomService = retrofit.create(RoomService.class);
        mUserService = retrofit.create(UserService.class);
        mProductService = retrofit.create(ProductService.class);
    }

    void registerProxyListener(ClientProxyListener listener) {
        if (!mProxyListeners.contains(listener)) {
            mProxyListeners.add(listener);
        }
    }

    void removeProxyListener(ClientProxyListener listener) {
        mProxyListeners.remove(listener);
    }

    void requestGiftList(long reqId) {
        mGeneralService.requestGiftList(reqId, Request.GIFT_LIST).enqueue(new Callback<GiftListResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<GiftListResponse> call, Response<GiftListResponse> response) {
                GiftListResponse giftListResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (giftListResponse == null) {
                        try {
                            listener.onResponseError(Request.GIFT_LIST, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (giftListResponse.code == ERROR_OK) {
                        listener.onGiftListResponse(giftListResponse);
                    } else {
                        listener.onResponseError(Request.GIFT_LIST, giftListResponse.code, giftListResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<GiftListResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.GIFT_LIST, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestMusicList(long reqId) {
        mGeneralService.requestMusicList(reqId, Request.MUSIC_LIST).enqueue(new Callback<MusicListResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<MusicListResponse> call, Response<MusicListResponse> response) {
                MusicListResponse musicListResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (musicListResponse == null) {
                        try {
                            listener.onResponseError(Request.MUSIC_LIST, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (musicListResponse.code == ERROR_OK) {
                        listener.onMusicLisResponse(musicListResponse);
                    } else {
                        listener.onResponseError(Request.MUSIC_LIST, musicListResponse.code, musicListResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<MusicListResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.MUSIC_LIST, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestOssPolicy(long reqId, String token, int type) {
        mGeneralService.requestOssPolicy(reqId, Request.OSS, token, type).enqueue(new Callback<OssPolicyResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<OssPolicyResponse> call, Response<OssPolicyResponse> response) {
                OssPolicyResponse ossPolicyResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (ossPolicyResponse == null) {
                        try {
                            listener.onResponseError(Request.OSS, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (ossPolicyResponse.code == ERROR_OK) {
                        listener.onOssPolicyResponse(ossPolicyResponse);
                    } else {
                        listener.onResponseError(Request.OSS, ossPolicyResponse.code, ossPolicyResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<OssPolicyResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.OSS, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }


    void requestRoomList(long reqId, String token, String nextId, int count, int type, Integer pkState) {
        mRoomService.requestRoomList(reqId, token, Request.ROOM_LIST, nextId, count, type, pkState).enqueue(new Callback<RoomListResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse roomListResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (roomListResponse == null) {
                        try {
                            listener.onResponseError(Request.ROOM_LIST, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (roomListResponse.code == ERROR_OK) {
                        listener.onRoomListResponse(roomListResponse);
                    } else {
                        listener.onResponseError(Request.ROOM_LIST, roomListResponse.code, roomListResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.ROOM_LIST, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void createRoom(long reqId, String token, String roomName, int type, String avatar) {
        mRoomService.requestCreateLiveRoom(token, reqId, Request.CREATE_ROOM,
                new CreateRoomRequestBody(roomName, type, avatar)).enqueue(new Callback<CreateRoomResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CreateRoomResponse> call, Response<CreateRoomResponse> response) {
                CreateRoomResponse createRoomResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (createRoomResponse == null) {
                        try {
                            listener.onResponseError(Request.CREATE_ROOM, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (createRoomResponse.code == ERROR_OK) {
                        listener.onCreateRoomResponse(createRoomResponse);
                    } else {
                        listener.onResponseError(Request.CREATE_ROOM, createRoomResponse.code, createRoomResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CreateRoomResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.CREATE_ROOM, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void enterRoom(long reqId, String token, String roomId) {
        mRoomService.requestEnterLiveRoom(token, reqId, Request.ENTER_ROOM, roomId).enqueue(new Callback<EnterRoomResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<EnterRoomResponse> call, Response<EnterRoomResponse> response) {
                EnterRoomResponse enterRoomResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (enterRoomResponse == null) {
                        try {
                            listener.onResponseError(Request.ENTER_ROOM, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (enterRoomResponse.code == ERROR_OK) {
                        listener.onEnterRoomResponse(enterRoomResponse);
                    } else {
                        listener.onResponseError(Request.ENTER_ROOM, enterRoomResponse.code, enterRoomResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<EnterRoomResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.ENTER_ROOM, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void leaveRoom(long reqId, String token, String roomId) {
        mRoomService.requestLeaveLiveRoom(token, reqId, Request.LEAVE_ROOM, roomId).enqueue(new Callback<LeaveRoomResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<LeaveRoomResponse> call, Response<LeaveRoomResponse> response) {
                LeaveRoomResponse leaveRoomResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (leaveRoomResponse == null) {
                        try {
                            listener.onResponseError(Request.LEAVE_ROOM, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (leaveRoomResponse.code == ERROR_OK) {
                        listener.onLeaveRoomResponse(leaveRoomResponse);
                    } else {
                        listener.onResponseError(Request.LEAVE_ROOM, leaveRoomResponse.code, leaveRoomResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<LeaveRoomResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.LEAVE_ROOM, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestAudienceList(long reqId, String token, String roomId, String nextId, int count ,int type) {
        mRoomService.requestAudienceList(token, reqId, Request.AUDIENCE_LIST,
                roomId, nextId, count, type).enqueue(new Callback<AudienceListResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<AudienceListResponse> call, Response<AudienceListResponse> response) {
                AudienceListResponse audienceListResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (audienceListResponse == null) {
                        try {
                            listener.onResponseError(Request.AUDIENCE_LIST, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (audienceListResponse.code == ERROR_OK) {
                        listener.onAudienceListResponse(audienceListResponse);
                    } else {
                        listener.onResponseError(Request.AUDIENCE_LIST, audienceListResponse.code, audienceListResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<AudienceListResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.AUDIENCE_LIST, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestSeatState(long reqId, String token, String roomId) {
        mRoomService.requestSeatState(token, reqId,
                Request.SEAT_STATE, roomId).enqueue(new Callback<SeatStateResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<SeatStateResponse> call, Response<SeatStateResponse> response) {
                SeatStateResponse seatStateResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (seatStateResponse == null) {
                        try {
                            listener.onResponseError(Request.SEAT_STATE, ERROR_NULL,
                                    response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (seatStateResponse.code == ERROR_OK) {
                        listener.onRequestSeatStateResponse(seatStateResponse);
                    } else {
                        listener.onResponseError(Request.SEAT_STATE, seatStateResponse.code, seatStateResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<SeatStateResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.SEAT_STATE, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void sendGift(long reqId, String token, String roomId, int giftId, int count) {
        mRoomService.requestSendGift(token, reqId, Request.SEND_GIFT,
                roomId, new SendGiftBody(giftId, count)).enqueue(new Callback<SendGiftResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<SendGiftResponse> call, Response<SendGiftResponse> response) {
                SendGiftResponse sendGiftResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (sendGiftResponse == null) {
                        listener.onResponseError(Request.SEND_GIFT, ERROR_NULL,
                                response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().toString());
                    } else if (sendGiftResponse.code == ERROR_OK) {
                        listener.onSendGiftResponse(sendGiftResponse);
                    } else {
                        listener.onResponseError(Request.SEND_GIFT, sendGiftResponse.code, sendGiftResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<SendGiftResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.SEND_GIFT, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void giftRank(long reqId, String roomId) {
        mRoomService.requestGiftRank(reqId, Request.GIFT_RANK,
                roomId).enqueue(new Callback<GiftRankResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<GiftRankResponse> call, Response<GiftRankResponse> response) {
                GiftRankResponse giftRankResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (giftRankResponse == null) {
                        listener.onResponseError(Request.GIFT_RANK, ERROR_NULL,
                                response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().toString());
                    } else if (giftRankResponse.code == ERROR_OK) {
                        listener.onGiftRankResponse(giftRankResponse);
                    } else {
                        listener.onResponseError(Request.GIFT_RANK, giftRankResponse.code, giftRankResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<GiftRankResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.GIFT_RANK, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void refreshToken(long reqId, String token, String roomId) {
        mGeneralService.requestRefreshToken(reqId, Request.REFRESH_TOKEN,
                token, roomId).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                RefreshTokenResponse refreshTokenResponse = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (refreshTokenResponse == null) {
                        listener.onResponseError(Request.REFRESH_TOKEN, ERROR_NULL,
                                response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().toString());
                    } else if (refreshTokenResponse.code == ERROR_OK) {
                        listener.onRefreshTokenResponse(refreshTokenResponse);
                    } else {
                        listener.onResponseError(Request.REFRESH_TOKEN, refreshTokenResponse.code, refreshTokenResponse.msg);
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.REFRESH_TOKEN, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }


    void requestProductList(String token, String roomId) {
        mProductService.requestProductList(token, roomId).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productList = response.body();
                for (ClientProxyListener listener : mProxyListeners) {
                    if (productList == null) {
                        listener.onResponseError(Request.PRODUCT_LIST, ERROR_NULL,
                                response.errorBody() == null ? MSG_NULL_RESPONSE : response.errorBody().toString());
                    } else if (productList.code == ERROR_OK) {
                        listener.onGetProductListResponse(productList);
                    } else {
                        listener.onResponseError(Request.PRODUCT_LIST, productList.code, productList.msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.PRODUCT_LIST, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestManageProductState(String token, String roomId, String productId, int state) {
        mProductService.requestManageProductState(token, roomId, productId, state).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse productManageResponse = response.body();
                if (productManageResponse == null || productManageResponse.code != ERROR_OK) {
                    for (ClientProxyListener listener : mProxyListeners) {
                        listener.onResponseError(Request.PRODUCT_MANAGE,
                                productManageResponse == null ? ERROR_NULL : productManageResponse.code,
                                productManageResponse == null ? MSG_NULL_RESPONSE : productManageResponse.msg);
                    }
                } else {
                    for (ClientProxyListener listener : mProxyListeners) {
                        listener.onProductStateChangedResponse(productId, state, productManageResponse.data);
                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                for (ClientProxyListener listener : mProxyListeners) {
                    listener.onResponseError(Request.PRODUCT_MANAGE, ERROR_CONNECTION, t.getMessage());
                }
            }
        });
    }

    void requestPurchaseProduct(String token, String roomId, String productId, int count) {
        mProductService.requestPurchaseProduct(token, roomId, new PurchaseProductBody(productId, count))
                .enqueue(new Callback<BooleanResponse>() {
                    @Override
                    public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                        BooleanResponse purchaseResponse = response.body();
                        if (purchaseResponse == null || purchaseResponse.code != ERROR_OK) {
                            for (ClientProxyListener listener : mProxyListeners) {
                                listener.onResponseError(Request.PRODUCT_MANAGE,
                                        purchaseResponse == null ? ERROR_NULL : purchaseResponse.code,
                                        purchaseResponse == null ? MSG_NULL_RESPONSE : purchaseResponse.msg);
                            }
                        } else {
                            for (ClientProxyListener listener : mProxyListeners) {
                                listener.onProductPurchasedResponse(purchaseResponse.data);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BooleanResponse> call, Throwable t) {
                        for (ClientProxyListener listener : mProxyListeners) {
                            listener.onResponseError(Request.PRODUCT_PURCHASE, ERROR_CONNECTION, t.getMessage());
                        }
                    }
                });
    }
}
