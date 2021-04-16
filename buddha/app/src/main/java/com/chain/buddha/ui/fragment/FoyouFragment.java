package com.chain.buddha.ui.fragment;

import android.content.pm.PackageManager;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chain.buddha.R;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.fragment.xiuxing.FanyinFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.vlive.AgoraLiveManager;
import io.agora.vlive.Config;
import io.agora.vlive.protocol.ClientProxyListener;
import io.agora.vlive.protocol.model.request.Request;
import io.agora.vlive.protocol.model.request.UserRequest;
import io.agora.vlive.protocol.model.response.AppVersionResponse;
import io.agora.vlive.protocol.model.response.AudienceListResponse;
import io.agora.vlive.protocol.model.response.CreateRoomResponse;
import io.agora.vlive.protocol.model.response.CreateUserResponse;
import io.agora.vlive.protocol.model.response.EditUserResponse;
import io.agora.vlive.protocol.model.response.EnterRoomResponse;
import io.agora.vlive.protocol.model.response.GiftListResponse;
import io.agora.vlive.protocol.model.response.GiftRankResponse;
import io.agora.vlive.protocol.model.response.LeaveRoomResponse;
import io.agora.vlive.protocol.model.response.LoginResponse;
import io.agora.vlive.protocol.model.response.ModifyUserStateResponse;
import io.agora.vlive.protocol.model.response.MusicListResponse;
import io.agora.vlive.protocol.model.response.OssPolicyResponse;
import io.agora.vlive.protocol.model.response.ProductListResponse;
import io.agora.vlive.protocol.model.response.RefreshTokenResponse;
import io.agora.vlive.protocol.model.response.Response;
import io.agora.vlive.protocol.model.response.RoomListResponse;
import io.agora.vlive.protocol.model.response.SeatStateResponse;
import io.agora.vlive.protocol.model.response.SendGiftResponse;
import io.agora.vlive.ui.main.fragments.RoomFragment;
import io.agora.vlive.utils.Global;
import io.agora.vlive.utils.RandomUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoyouFragment extends BaseFragment implements ClientProxyListener {

    public FoyouFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    List<Fragment> fragmentList;
    String[] mTabs;
    FmPagerAdapter adapter;


    @Override
    protected int setLayout() {
        return R.layout.fragment_foyou;
    }

    @Override
    public void onStart() {
        super.onStart();
        AgoraLiveManager.getInstance().proxy().registerProxyListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AgoraLiveManager.getInstance().proxy().removeProxyListener(this);
    }

    @Override
    protected void init() {
        int index = 0;
        mTabs = new String[]{"直播"};
        fragmentList = new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            switch (i) {
                case 0:
                    fragmentList.add(new RoomFragment());
                    break;
                case 1:
                    fragmentList.add(new FanyinFragment());
                    break;
                default:
                    break;
            }
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, false);
        adapter = new FmPagerAdapter(fragmentList, getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < fragmentList.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTabs[i]);
        }
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void lazyInit() {
        initAsync();
    }


    private void initAsync() {
        new Thread(() -> {
            checkUpdate();
            getGiftList();
        }).start();
    }

    private void checkUpdate() {
        if (!AgoraLiveManager.getInstance().config().hasCheckedVersion()) {
            AgoraLiveManager.getInstance().proxy().sendRequest(Request.APP_VERSION, "3.2.0");
        }
    }


    @Override
    public void onAppVersionResponse(AppVersionResponse response) {
        AgoraLiveManager.getInstance().config().setVersionInfo(response.data);
        AgoraLiveManager.getInstance().config().setAppId(response.data.config.appId);
        AgoraLiveManager.getInstance().initEngine(response.data.config.appId);
        login();
    }

    @Override
    public void onRefreshTokenResponse(RefreshTokenResponse refreshTokenResponse) {

    }

    @Override
    public void onOssPolicyResponse(OssPolicyResponse response) {

    }

    @Override
    public void onMusicLisResponse(MusicListResponse response) {

    }

    private void login() {
        Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
        initUserFromStorage(profile);
        if (!profile.isValid()) {
            createUser();
        } else {
            loginToServer();
        }
    }

    private void initUserFromStorage(Config.UserProfile profile) {
        profile.setUserId(AgoraLiveManager.getInstance().preferences().getString(Global.Constants.KEY_PROFILE_UID, null));
        profile.setUserName(AgoraLiveManager.getInstance().preferences().getString(Global.Constants.KEY_USER_NAME, null));
        profile.setImageUrl(AgoraLiveManager.getInstance().preferences().getString(Global.Constants.KEY_IMAGE_URL, null));
        profile.setToken(AgoraLiveManager.getInstance().preferences().getString(Global.Constants.KEY_TOKEN, null));
    }

    private void createUser() {
        String userName = RandomUtil.randomUserName(mContext);
        AgoraLiveManager.getInstance().config().getUserProfile().setUserName(userName);
        AgoraLiveManager.getInstance().preferences().edit().putString(Global.Constants.KEY_USER_NAME, userName).apply();
        AgoraLiveManager.getInstance().proxy().sendRequest(Request.CREATE_USER, new UserRequest(userName));
    }

    @Override
    public void onCreateUserResponse(CreateUserResponse response) {
        createUserFromResponse(response);
        loginToServer();
    }

    @Override
    public void onEditUserResponse(EditUserResponse response) {

    }

    private void createUserFromResponse(CreateUserResponse response) {
        Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
        profile.setUserId(response.data.userId);
        AgoraLiveManager.getInstance().preferences().edit().putString(Global.Constants.KEY_PROFILE_UID, profile.getUserId()).apply();
    }

    private void loginToServer() {
        AgoraLiveManager.getInstance().proxy().sendRequest(Request.USER_LOGIN, AgoraLiveManager.getInstance().config().getUserProfile().getUserId());
    }

    @Override
    public void onLoginResponse(LoginResponse response) {
        if (response != null && response.code == Response.SUCCESS) {
            Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
            profile.setToken(response.data.userToken);
            profile.setRtmToken(response.data.rtmToken);
            profile.setAgoraUid(response.data.uid);
            AgoraLiveManager.getInstance().preferences().edit().putString(Global.Constants.KEY_TOKEN, response.data.userToken).apply();
            joinRtmServer();
        }
    }

    @Override
    public void onCreateRoomResponse(CreateRoomResponse response) {

    }

    @Override
    public void onEnterRoomResponse(EnterRoomResponse response) {

    }

    @Override
    public void onLeaveRoomResponse(LeaveRoomResponse response) {

    }

    @Override
    public void onAudienceListResponse(AudienceListResponse response) {

    }

    @Override
    public void onRequestSeatStateResponse(SeatStateResponse response) {

    }

    @Override
    public void onModifyUserStateResponse(ModifyUserStateResponse response) {

    }

    @Override
    public void onSendGiftResponse(SendGiftResponse response) {

    }

    @Override
    public void onGiftRankResponse(GiftRankResponse response) {

    }

    @Override
    public void onGetProductListResponse(ProductListResponse response) {

    }

    @Override
    public void onProductStateChangedResponse(String productId, int state, boolean success) {

    }

    @Override
    public void onProductPurchasedResponse(boolean success) {

    }

    @Override
    public void onSeatInteractionResponse(long processId, String userId, int seatNo, int type) {

    }

    private void joinRtmServer() {
        Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
        AgoraLiveManager.getInstance().rtmClient().login(profile.getRtmToken(), String.valueOf(profile.getAgoraUid()), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {

            }
        });
    }

    private void getGiftList() {
        AgoraLiveManager.getInstance().proxy().sendRequest(Request.GIFT_LIST, null);
    }

    @Override
    public void onGiftListResponse(GiftListResponse response) {
        AgoraLiveManager.getInstance().config().initGiftList(mContext);
    }

    @Override
    public void onRoomListResponse(RoomListResponse response) {

    }

    @Override
    public void onResponseError(int requestType, int error, String message) {
        Log.e("request:", requestType + " error:" + error + " msg:" + message);

        switch (requestType) {
            default:
//                runOnUiThread(() -> showLongToast("Request type: " +
//                        Request.getRequestString(requestType) + " " + message));
        }
    }
}
