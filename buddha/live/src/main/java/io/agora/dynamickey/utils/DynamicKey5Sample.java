package io.agora.dynamickey.utils;

import io.agora.dynamickey.media.DynamicKey5;

import java.util.Date;
import java.util.Random;

/**
 * Created by Li on 10/1/2016.
 */
public class DynamicKey5Sample {
    static String appID = "cdb3a6e3819e4077959a7290f27f1d7c";
    static String appCertificate = "8f5c7a2d3a734c74897302bf05793602";
    static String channel = "7d72365eb983485397e3e3f9d460bdda";
    static int ts = (int)(new Date().getTime()/1000);
    static int r = new Random().nextInt();
    static long uid = 2882341273L;
    static int expiredTs = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(DynamicKey5.generateMediaChannelKey(appID, appCertificate, channel, ts, r, uid, expiredTs));
        System.out.println(DynamicKey5.generateRecordingKey(appID, appCertificate, channel, ts, r, uid, expiredTs));
        System.out.println(DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channel, ts, r, uid, expiredTs, DynamicKey5.noUpload));
        System.out.println(DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channel, ts, r, uid, expiredTs, DynamicKey5.audioVideoUpload));
    }
}
