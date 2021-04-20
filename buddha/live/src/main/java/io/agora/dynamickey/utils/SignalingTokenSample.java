package io.agora.dynamickey.utils;

import io.agora.dynamickey.signal.SignalingToken;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SignalingTokenSample {

    public static void main(String []args) throws NoSuchAlgorithmException{

        String appId = "cdb3a6e3819e4077959a7290f27f1d7c";
        String certificate = "5cfd2fd1755d40ecb72977518be15d3b";
        String account = "TestAccount";
        //Use the current time plus an available time to guarantee the only time it is obtained
        int expiredTsInSeconds = 1446455471 + (int) (new Date().getTime()/1000l);
        String result = SignalingToken.getToken(appId, certificate, account, expiredTsInSeconds);
        System.out.println(result);

    }
}
