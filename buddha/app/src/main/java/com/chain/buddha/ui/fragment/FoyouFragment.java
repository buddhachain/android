package com.chain.buddha.ui.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chain.buddha.R;
import com.chain.buddha.Xuper.BaseObserver;
import com.chain.buddha.Xuper.ResponseCallBack;
import com.chain.buddha.Xuper.XuperAccount;
import com.chain.buddha.adapter.FmPagerAdapter;
import com.chain.buddha.ui.BaseFragment;
import com.chain.buddha.ui.fragment.xiuxing.FanyinFragment;
import com.chain.buddha.ui.live.base.RoomFragment;
import com.google.android.material.tabs.TabLayout;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import io.agora.dynamickey.utils.AgoraConfigs;
import io.agora.dynamickey.utils.RtmTokenBuilderUtils;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.vlive.AgoraLiveManager;
import io.agora.vlive.Config;
import io.agora.vlive.protocol.ClientProxyListener;
import io.agora.vlive.protocol.model.request.Request;
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
import io.agora.vlive.retrofit.RetrofitUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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
        initAccount();
        new Thread(() -> {
            getGiftList();
        }).start();
    }


    private void initAccount() {
        AgoraLiveManager.getInstance().config().setAppId(AgoraConfigs.appId);
        AgoraLiveManager.getInstance().initEngine(AgoraConfigs.appId);
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
        profile.setUserId(XuperAccount.getAddress());
        loginToServer();
    }

    private void loginToServer() {
        String rtmToken = RtmTokenBuilderUtils.getRtmToken(XuperAccount.getAddress());
        Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
        profile.setRtmToken(rtmToken);
        profile.setAgoraUid(0);
        RetrofitUtil.getInstance().getService().login(rtmToken, XuperAccount.getAddress())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new BaseObserver<>(false, new ResponseCallBack<Response<Object>>() {
                    @Override
                    public void onSuccess(Response<Object> stringResponse) {
                        Log.e("登录成功", "登录成功");
                        joinRtmServer();
                    }

                    @Override
                    public void onFail(String message) {
                        Log.e("登录失败", "登录失败");
                    }
                }));
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


    private void joinRtmServer() {
        Config.UserProfile profile = AgoraLiveManager.getInstance().config().getUserProfile();
        AgoraLiveManager.getInstance().rtmClient().login(profile.getRtmToken(), XuperAccount.getAddress(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("登录成功", "登录成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e("登录失败", errorInfo.toString());
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
    }
}
