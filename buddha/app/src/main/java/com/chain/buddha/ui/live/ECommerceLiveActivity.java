package com.chain.buddha.ui.live;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperApi;
import com.chain.buddha.ui.activity.ShanjvDetailActivity;
import com.chain.buddha.ui.live.actionsheets.ProductActionSheet;
import com.chain.buddha.ui.live.base.LiveRoomActivity;
import com.chain.buddha.utils.SkipInsideUtil;
import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import io.agora.rtc.Constants;
import io.agora.vlive.R;
import io.agora.vlive.protocol.manager.ProductServiceManager;
import io.agora.vlive.protocol.model.model.Product;
import io.agora.vlive.protocol.model.model.SeatInfo;
import io.agora.vlive.protocol.model.model.UserProfile;
import io.agora.vlive.protocol.model.request.AudienceListRequest;
import io.agora.vlive.protocol.model.request.Request;
import io.agora.vlive.protocol.model.response.AudienceListResponse;
import io.agora.vlive.protocol.model.response.EnterRoomResponse;
import io.agora.vlive.protocol.model.response.ProductListResponse;
import io.agora.vlive.protocol.model.response.Response;
import io.agora.vlive.protocol.model.response.RoomListResponse;
import io.agora.vlive.ui.actionsheets.LiveRoomUserListActionSheet;
import io.agora.vlive.ui.actionsheets.toolactionsheet.AbsToolActionSheet;
import io.agora.vlive.ui.actionsheets.toolactionsheet.ECommerceToolActionSheet;
import io.agora.vlive.ui.components.CameraTextureView;
import io.agora.vlive.ui.components.LiveHostNameLayout;
import io.agora.vlive.ui.components.LiveMessageEditLayout;
import io.agora.vlive.ui.components.bottomLayout.AbsBottomLayout;
import io.agora.vlive.ui.components.bottomLayout.ECommerceBottomLayout;
import io.agora.vlive.utils.Global;
import io.agora.vlive.utils.UserUtil;

public class ECommerceLiveActivity extends LiveRoomActivity
        implements View.OnClickListener {
    private static final String TAG = ECommerceLiveActivity.class.getSimpleName();

    private static final int PK_RESULT_DISPLAY_LAST = 2000;

    private String mOwnerName;

    private LiveHostNameLayout mNamePad;
    private FrameLayout mBigVideoLayout;
    private boolean mTopLayoutCalculated;

    private ECommerceBottomLayout mBottomLayout;

    private boolean mAudioMuted;
    private boolean mVideoMuted;
    private boolean mInEarMonitoring = false;

    private ProductServiceManager mProductManager;

    private ProductActionSheet mProductListActionSheet;
    private LiveRoomUserListActionSheet mRoomUserListActionSheet;

    private ProductDetailWindow mProductDetailWindow;

    private AbsBottomLayout.BottomButtonListener mBottomListener = new AbsBottomLayout.BottomButtonListener() {
        @Override
        public void onLiveBottomLayoutShowMessageEditor() {
            if (messageEditLayout != null) {
                messageEditLayout.setVisibility(View.VISIBLE);
                messageEditText.requestFocus();
                inputMethodManager.showSoftInput(messageEditText, 0);
            }
        }

        @Override
        public void onFun1ButtonClicked(int role) {
            showActionSheetDialog(ACTION_SHEET_GIFT, tabIdToLiveType(tabId),
                    false, true, ECommerceLiveActivity.this);
        }


        @Override
        public void onFun2ButtonClicked(int role) {
            mProductListActionSheet = (ProductActionSheet)
                    showActionSheetDialog(ACTION_SHEET_PRODUCT_LIST, tabIdToLiveType(tabId),
                            isOwner, true, null);
            mProductListActionSheet.setProductManager(mProductManager);
            mProductListActionSheet.setRoomId(roomId);
            mProductListActionSheet.setRole(isOwner ?
                    Global.Constants.ROLE_OWNER : Global.Constants.ROLE_AUDIENCE);
            mProductListActionSheet.setListener(mProductActionListener);

            XuperApi.requestQifuList(new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String resp) {
                    try {
                        resp = resp.replaceAll("\\}", "");
                        String[] list = resp.split("\\{");
                        List<String> mQifuList = new ArrayList<>(Arrays.asList(list));
                        mQifuList.remove(0);
                        // 反转lists
                        Collections.reverse(mQifuList);
                        List<Product> products = new ArrayList<>();
                        for (String qifu : mQifuList) {
                            String[] args = qifu.split(",");
                            Product product = new Product();
                            product.productId = args[0];
                            product.description = args[1];
                            product.price = 100;
                            product.state = Product.PRODUCT_LAUNCHED;
                            products.add(product);
                        }

                        mProductListActionSheet.updateList(products);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String message) {
                }
            });
        }

        @Override
        public void onMoreButtonClicked() {
            ECommerceToolActionSheet actionSheet = new ECommerceToolActionSheet(ECommerceLiveActivity.this);
            actionSheet.setOnToolActionSheetItemClickedListener(mToolActionSheetItemListener);
            actionSheet.setRole(roomRoleToToolActionRole());
            showCustomActionSheetDialog(true, actionSheet);
        }

        private int roomRoleToToolActionRole() {
            if (isOwner) return AbsToolActionSheet.ROLE_OWNER;
            else return AbsToolActionSheet.ROLE_AUDIENCE;
        }

        @Override
        public void onCloseButtonClicked() {
            checkBeforeLeavingRoom();
        }
    };

    private void checkBeforeLeavingRoom() {

        int messageRes = isOwner
                ? R.string.end_live_streaming_message_owner
                : R.string.finish_broadcast_message_audience;
        curDialog = showDialog(R.string.end_live_streaming_title_owner,
                messageRes, ECommerceLiveActivity.this);
    }

    private ProductActionSheet.OnProductActionListener mProductActionListener
            = new ProductActionSheet.OnProductActionListener() {
        @Override
        public void onProductDetail(Product product) {
            showProductDetailWindow(product);
        }

        private void showProductDetailWindow(Product product) {
            SkipInsideUtil.skipInsideActivity(mContext, ShanjvDetailActivity.class, SkipInsideUtil.SKIP_KEY_KDID, product.productId);

//            mProductDetailWindow = new ProductDetailWindow(
//                    ECommerceLiveActivity.this,
//                    R.style.product_detail_window, product);
//            mProductDetailWindow.show();
        }

        @Override
        public void onProductListed(String productId) {
            XLog.d("onProductListed " + productId);
            mProductManager.requestChangeProductState(roomId, productId, Product.PRODUCT_LAUNCHED);
        }

        @Override
        public void onProductUnlisted(String productId) {
            XLog.d("onProductUnlisted " + productId);
            mProductManager.requestChangeProductState(roomId, productId, Product.PRODUCT_UNAVAILABLE);
        }
    };


    @Override
    public void onProductStateChangedResponse(String productId, int state, boolean success) {
        runOnUiThread(() -> {
            if (state == Product.PRODUCT_LAUNCHED && success) {
                showShortToast(getString(R.string.product_list_success));
            } else if (state == Product.PRODUCT_UNAVAILABLE && success) {
                showShortToast(getString(R.string.product_unlist_success));
            }

            mProductManager.requestProductList(roomId);
        });
    }

    @Override
    public void onRoomListResponse(RoomListResponse response) {
        super.onRoomListResponse(response);
    }

    private AbsToolActionSheet.OnToolActionSheetItemClickedListener
            mToolActionSheetItemListener = new AbsToolActionSheet.OnToolActionSheetItemClickedListener() {
        public void onToolActionSheetItemClicked(int position, View view) {
            switch (position) {
                case 0:
                    if (isOwner) {
                        onActionSheetSettingClicked();
                    } else {
                        dismissActionSheetDialog();
                    }
                    break;
                case 1:
                    onActionSheetRealDataClicked();
                    break;
                case 2:
                    showActionSheetDialog(ACTION_SHEET_BEAUTY, tabIdToLiveType(tabId),
                            true, true, ECommerceLiveActivity.this);
                    break;
                case 3:
                    showActionSheetDialog(ACTION_SHEET_BG_MUSIC, tabIdToLiveType(tabId),
                            true, true, ECommerceLiveActivity.this);
                    break;
                case 4:
                    onActionSheetRotateClicked();
                    break;
                case 5:
                    mVideoMuted = !mVideoMuted;
                    rtcEngine().muteLocalVideoStream(mVideoMuted);
                    config().setVideoMuted(mVideoMuted);
                    break;
                case 6:
                    mAudioMuted = !mAudioMuted;
                    rtcEngine().muteLocalAudioStream(mAudioMuted);
                    config().setAudioMuted(mAudioMuted);
                    break;
                case 7:
                    if (onActionSheetEarMonitoringClicked(!mInEarMonitoring)) {
                        mInEarMonitoring = !mInEarMonitoring;
                    }
                    break;
            }
        }

        public void onToolActionSheetItemViewBind(int position, View view) {
            switch (position) {
                case 5:
                    view.setActivated(!config().isVideoMuted());
                    break;
                case 6:
                    view.setActivated(!config().isAudioMuted());
                    break;
                case 7:
                    view.setActivated(mInEarMonitoring);
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(false);
    }

    @Override
    protected void onPermissionGranted() {
        initUI();
        mProductManager = new ProductServiceManager(application());
        super.onPermissionGranted();
    }

    private void initUI() {
        setContentView(R.layout.activity_ecommerce);

        mNamePad = findViewById(R.id.ecommerce_name_pad);
        mNamePad.init();

        participants = findViewById(R.id.ecommerce_participant);
        participants.init();
        participants.setUserLayoutListener(this);

        mBottomLayout = findViewById(R.id.ecommerce_bottom_layout);
        mBottomLayout.setBottomLayoutListener(mBottomListener);

        mBigVideoLayout = findViewById(R.id.ecommerce_big_video_layout);

        if (isOwner) {
            mAudioMuted = false;
            mVideoMuted = false;
            becomesOwner(false, false);
        }

        messageList = findViewById(R.id.message_list);
        messageList.init();
        messageEditLayout = findViewById(R.id.message_edit_layout);
        messageEditText = messageEditLayout.findViewById(LiveMessageEditLayout.EDIT_TEXT_ID);

        rtcStatsView = findViewById(R.id.single_host_rtc_stats);
        rtcStatsView.setCloseListener(view -> rtcStatsView.setVisibility(View.GONE));

        // In case that the UI is not relocated because
        // the permission request dialog consumes the chance
        onGlobalLayoutCompleted();

    }

    @Override
    protected void onGlobalLayoutCompleted() {
        View topLayout = findViewById(R.id.ecommerce_participant_layout);
        if (topLayout != null && !mTopLayoutCalculated) {
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
            params.topMargin += systemBarHeight;
            topLayout.setLayoutParams(params);
            mTopLayoutCalculated = true;
        }
    }

    @Override
    public void onEnterRoomResponse(EnterRoomResponse response) {
        super.onEnterRoomResponse(response);
        if (response.code == Response.SUCCESS) {
            ownerId = response.data.room.owner.userId;
            ownerRtcUid = response.data.room.owner.uid;
            mOwnerName = response.data.room.owner.userName;

            // Determine if I am the owner of a host here because
            // I may leave the room unexpectedly and come once more.
            String myId = config().getUserProfile().getUserId();
            if (!isOwner && myId.equals(response.data.room.owner.userId)) {
                isOwner = true;
            }

            if (isOwner) {
                mAudioMuted = response.data.room.owner.enableAudio !=
                        SeatInfo.User.USER_AUDIO_ENABLE;
                mVideoMuted = response.data.room.owner.enableVideo !=
                        SeatInfo.User.USER_VIDEO_ENABLE;
            }

            // Check if someone is the host
            boolean callConnected = false;
            boolean iamHost = false;
            List<SeatInfo> seatListInfo = response.data.room.coVideoSeats;
            if (seatListInfo.size() > 0) {
                SeatInfo info = seatListInfo.get(0);
                if (info.seat.state == SeatInfo.TAKEN) {
                    callConnected = true;
                    iamHost = myId.equals(info.user.userId);

                    if (iamHost) {
                        mAudioMuted = info.user.enableAudio !=
                                SeatInfo.User.USER_AUDIO_ENABLE;
                        mVideoMuted = info.user.enableVideo !=
                                SeatInfo.User.USER_VIDEO_ENABLE;
                    }
                }
            }

            final boolean inCall = callConnected;
            final boolean callHost = iamHost;

            if (!isOwner && !callHost) {
                mAudioMuted = true;
                mVideoMuted = true;
            }

            runOnUiThread(() -> {
                if (isOwner) {
                    becomesOwner(mAudioMuted, mVideoMuted);
                } else {
                    becomeAudience();
                }

                mNamePad.setName(response.data.room.owner.userName);
                mNamePad.setIcon(UserUtil.getUserRoundIcon(getResources(),
                        response.data.room.owner.userId));
            });
        }
    }

    private void becomesOwner(boolean audioMuted, boolean videoMuted) {
        if (!videoMuted) startCameraCapture();
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        mBottomLayout.setRole(bottomLayoutRole());
        config().setAudioMuted(audioMuted);
        config().setVideoMuted(videoMuted);
        initLocalPreview();
    }

    private void initLocalPreview() {
        CameraTextureView textureView = new CameraTextureView(this);
        mBigVideoLayout.addView(textureView);
    }

    private void becomeAudience() {
        isHost = false;
        stopCameraCapture();
        mBottomLayout.setRole(bottomLayoutRole());
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        config().setAudioMuted(true);
        config().setVideoMuted(true);
        setupRemotePreview();
    }

    private void setupRemotePreview() {
        SurfaceView surfaceView = setupRemoteVideo(ownerRtcUid);
        mBigVideoLayout.addView(surfaceView);
    }

    private int bottomLayoutRole() {
        if (isOwner) return AbsBottomLayout.ROLE_OWNER;
        else if (isHost) return AbsBottomLayout.ROLE_HOST;
        else return AbsBottomLayout.ROLE_AUDIENCE;
    }

    @Override
    public void onBackPressed() {
        checkBeforeLeavingRoom();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.dialog_positive_button) {
            leaveRoom();
            finish();
        } else if (id == R.id.remote_call_close_btn) {
        }
    }

    @Override
    public void onUserLayoutShowUserList(View view) {
        // Show invite user list
        if (isOwner) {
            requestAudienceList();
        } else {
            mRoomUserListActionSheet = (LiveRoomUserListActionSheet)
                    showActionSheetDialog(ACTION_SHEET_ROOM_USER, tabIdToLiveType(tabId), isHost, true, this);
//            mRoomUserListActionSheet.setup(proxy(), this, roomId, config().getUserProfile().getToken());
            mRoomUserListActionSheet.requestMoreAudience();
        }
    }

    private void requestAudienceList() {
//        sendRequest(Request.AUDIENCE_LIST, new AudienceListRequest(
//                config().getUserProfile().getToken(),
//                roomId, null, AudienceListRequest.TYPE_ALL));
    }

    @Override
    public void onAudienceListResponse(AudienceListResponse response) {

        List<UserProfile> userList = new ArrayList<>();
        for (AudienceListResponse.AudienceInfo info : response.data.list) {
            UserProfile profile = new UserProfile();
            profile.setUserId(info.userId);
            profile.setUserName(info.userName);
            profile.setAvatar(info.avatar);
            userList.add(profile);
        }

        if (mRoomUserListActionSheet != null &&
                mRoomUserListActionSheet.isShown()) {
            runOnUiThread(() -> mRoomUserListActionSheet.appendUsers(userList));
        }
    }


    @Override
    public void onGetProductListResponse(ProductListResponse response) {
        runOnUiThread(() -> {
            if (mProductListActionSheet != null
                    && mProductListActionSheet.isShown()) {
                mProductListActionSheet.updateList(response.data);
            }
        });
    }

    @Override
    public void onRtmProductPurchased(String productId, int count) {
        mProductManager.requestProductList(roomId);
    }

    @Override
    public void onRtmProductStateChanged(String productId, int state) {
        if (!isOwner && state == Product.PRODUCT_LAUNCHED) {
            if (!isCurDialogShowing() && !actionSheetShowing()) {
                showShortToast("有新商品上架");
            }
        }
    }

    @Override
    public void onProductPurchasedResponse(boolean success) {
        int toastRes = success ? R.string.product_purchase_success : R.string.product_purchase_fail;
        runOnUiThread(() -> showShortToast(getString(toastRes)));
    }


    private void updatePkGiftRank(int mine, int other) {

    }


    private class ProductDetailWindow extends Dialog {
        private Product mProduct;
        private RelativeLayout mVideo;
        private RelativeLayout mOwnerVideoLayout;
        private int mPictureRes;

        public ProductDetailWindow(@NonNull Context context, int styleRes,
                                   Product product) {
            super(context, styleRes);
            mProduct = product;
            mPictureRes = productIdToPictureResource(mProduct.productId);
        }

        @Override
        public void show() {
            setContentView(R.layout.product_detail_layout);
            hideStatusBar(getWindow(), true);
            setCancelable(true);
            AppCompatTextView buyBtn = findViewById(R.id.product_buy_now_btn);
            buyBtn.setOnClickListener(view -> {
                if (mProductManager != null) {
                    mProductManager.requestPurchaseProduct(roomId, mProduct.productId, 1);
                }
                dismiss();
            });

            findViewById(R.id.product_detail_back).setOnClickListener(view -> dismissProductDetailWindow());
            findViewById(R.id.product_detail_video_close_btn).setOnClickListener(
                    view -> {
                        if (mOwnerVideoLayout != null) {
                            ViewGroup parent = (ViewGroup) mOwnerVideoLayout.getParent();
                            parent.removeView(mOwnerVideoLayout);
                        }
                    });

            AppCompatTextView productDescription = findViewById(R.id.product_window_description_text);
            productDescription.setText(parseDescription(mProduct.productId));

            AppCompatImageView pictureImageView = findViewById(R.id.product_detail_big_picture);
            pictureImageView.setImageResource(mPictureRes);

            mOwnerVideoLayout = findViewById(R.id.product_detail_owner_video_layout);
            mOwnerVideoLayout.setVisibility(View.VISIBLE);

            mVideo = findViewById(R.id.owner_video);
            SurfaceView surfaceView = setupRemoteVideo(ownerRtcUid);
            mVideo.removeAllViews();
            mVideo.addView(surfaceView);

            mBigVideoLayout.removeAllViews();

            super.show();
        }

        private int parseDescription(String productId) {
            switch (productId) {
                case "2":
                    return R.string.product_desp_2;
                case "3":
                    return R.string.product_desp_3;
                case "4":
                    return R.string.product_desp_4;
                default:
                    return R.string.product_desp_1;
            }
        }

        @Override
        public void dismiss() {
            if (mOwnerVideoLayout != null) {
                mOwnerVideoLayout.removeAllViews();
            }

            SurfaceView surfaceView = setupRemoteVideo(ownerRtcUid);

            mBigVideoLayout.addView(surfaceView);

            super.dismiss();
        }

        int productIdToPictureResource(String id) {
            switch (id) {
                default:
                    return R.drawable.icon_product_1;
            }
        }
    }

    @Override
    public void onRtmLeaveMessage() {
        runOnUiThread(() -> {
            dismissProductDetailWindow();
            leaveRoom();
        });
    }

    private void dismissProductDetailWindow() {
        Log.d(TAG, "dismiss product");
        if (mProductDetailWindow != null &&
                mProductDetailWindow.isShowing()) {
            mProductDetailWindow.dismiss();
        }
    }
}
