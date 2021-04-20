package io.agora.dynamickey.utils;

import io.agora.dynamickey.media.RtcTokenBuilder;
import io.agora.dynamickey.media.RtcTokenBuilder.Role;

public class RtcTokenBuilderUtils {
    static int expirationTimeInSeconds = 3600;

    public static String getRtcToken(String channelName, String userAccount) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(AgoraConfigs.appId, AgoraConfigs.appCertificate,
                channelName, userAccount, Role.Role_Publisher, timestamp);

//        result = token.buildTokenWithUid(appId, appCertificate,
//                channelName, uid, Role.Role_Publisher, timestamp);
        return result;
    }
}
