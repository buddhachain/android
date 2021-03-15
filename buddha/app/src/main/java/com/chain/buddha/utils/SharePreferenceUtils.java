package com.chain.buddha.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.chain.buddha.BuddhaApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SharePreferenceUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String SAVE_FILE_NAME_DEFAULT = "share_data";
    public static String SAVE_FILE_NAME = SAVE_FILE_NAME_DEFAULT;

    private static final int SAVE_MODE = Context.MODE_PRIVATE;


    public static void putString(String key, String value){
        SharedPreferences sharedPreferences= BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        sharedPreferences.edit().putString(key,value).commit();
    }

    public static String getString(String key){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        return sharedPreferences.getString(key,"");
    }

    public static void putObject(String key, Object object){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        try{
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(object);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            edit.putString(key, bytesToHexString).commit();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Object getObject(String key){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        String string = sharedPreferences.getString(key, "");
        if (TextUtils.isEmpty(string)){
            return null;
        }
        try {
            byte[] stringToBytes = StringToBytes(string);
            ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
            ObjectInputStream is = new ObjectInputStream(bis);
            //返回反序列化得到的对象
            Object readObject = is.readObject();
            return readObject;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean contains(String key){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        return sharedPreferences.contains(key);
    }

    public static void remove(String key){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        sharedPreferences.edit().remove(key).commit();
    }

    public static void clear(){
        SharedPreferences sharedPreferences=BuddhaApplication.getInstance().getSharedPreferences(SAVE_FILE_NAME,SAVE_MODE);
        sharedPreferences.edit().clear().commit();
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

}
