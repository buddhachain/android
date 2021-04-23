package com.chain.buddha.ui.live;

import com.chain.buddha.ui.live.base.AbsPageFragment;

import io.agora.vlive.protocol.ClientProxy;

public class ECommerceFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_ECOMMERCE;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return ECommerceLiveActivity.class;
    }
}
