package io.agora.dynamickey.utils;

import io.agora.dynamickey.rtm.RtmTokenBuilder;
import io.agora.dynamickey.rtm.RtmTokenBuilder.Role;

public class RtmTokenBuilderUtils {
    private static int expireTimestamp = 3600;

    public static String getRtmToken(String userId) {
        RtmTokenBuilder token = new RtmTokenBuilder();
        String result = null;
        try {
            result = token.buildToken(AgoraConfigs.appId, AgoraConfigs.appCertificate, userId, Role.Rtm_User, expireTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
