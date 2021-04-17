package com.chain.buddha.ui.live;

import io.agora.vlive.protocol.ClientProxy;
import io.agora.vlive.ui.main.fragments.AbsPageFragment;

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
