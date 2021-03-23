package com.chain.buddha.utils;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理
 *
 * @author Ryan
 */
public class StringUtils {

    private StringUtils() { /* cannot be instantiated */
    }

    /**
     * Check a string is empty or not.
     *
     * @param str
     * @return boolean
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isBlank(null) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank(" ") = true
     * StringUtils.isBlank("bob") = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsNull(String str) {
        return isBlank(str) || str.equalsIgnoreCase("null");
    }

    public static boolean equalsHasNull(String... str) {
        boolean hasNullEqual = false;
        for (int i = 0; i < str.length; i++) {
            if (equalsNull(str[i]))
                hasNullEqual = true;
        }
        return hasNullEqual;
    }

    /**
     * <p>
     * Checks if the String contains only unicode digits. A decimal point is not
     * a unicode digit and
     * returns false.
     * </p>
     * <p>
     * <code>null</code> will return <code>false</code>. An empty String
     * (length()=0) will return
     * <code>true</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isNumeric(null) = false
     * StringUtils.isNumeric(&quot;&quot;) = true
     * StringUtils.isNumeric(&quot; &quot;) = false
     * StringUtils.isNumeric(&quot;123&quot;) = true
     * StringUtils.isNumeric(&quot;12 3&quot;) = false
     * StringUtils.isNumeric(&quot;ab2c&quot;) = false
     * StringUtils.isNumeric(&quot;12-3&quot;) = false
     * StringUtils.isNumeric(&quot;12.3&quot;) = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5Helper(String plainText) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            final byte b[] = md.digest();
            int i;
            final StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();// 32位的加密
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSHA1Hash(String string) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-1");
            sha256.update(string.getBytes());
            return Hex.toHexString(sha256.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSubStr(String str, int subNu, String replace) {

        final int strLength = str.length();
        if (strLength >= subNu) {
            str = str.substring((strLength - subNu), strLength);
        } else {
            for (int i = strLength; i < subNu; i++) {
                str += replace;
            }
        }
        return str;
    }


    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        final Pattern pattern = Pattern.compile("[0-9]{1,}");
        final Matcher matcher = pattern.matcher(strNum);
        return matcher.matches();
    }

    /**
     * 字符串变大写
     *
     * @param s
     * @return
     */
    public static String stringChangeCapital(String s) {
        if (equalsNull(s)) {
            return "";
        }
        final char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (c[i] >= 'a' && c[i] <= 'z') {
                c[i] = Character.toUpperCase(c[i]);
            } else if (c[i] >= 'A' && c[i] <= 'Z') {
                c[i] = Character.toLowerCase(c[i]);
            }
        }
        return String.valueOf(c);
    }

    public static int getIntValue(String strTemp) {
        int nRet = 0;

        try {
            if (!equalsNull(strTemp)) {
                nRet = Integer.parseInt(strTemp);
            }
        } catch (Exception ex) {
        }

        return nRet;
    }

    public static long getLongValue(String strTemp) {
        long nRet = 0;

        try {
            if (strTemp == null) {
                return nRet;
            }

            nRet = (long) Double.parseDouble(strTemp);
        } catch (Exception ex) {
            nRet = 0;
        }

        return nRet;
    }

    public static double getDoubleValue(String strTemp) {
        double nRet = 0;

        try {
            if (StringUtils.equalsNull(strTemp)) {
                return nRet;
            }

            nRet = Double.parseDouble(strTemp);
        } catch (Exception ex) {
            nRet = 0;
        }

        return nRet;
    }

    public static BigDecimal getBigDecimalValue(String strTemp) {
        BigDecimal nRet = BigDecimal.ZERO;

        try {
            if (StringUtils.equalsNull(strTemp)) {
                return nRet;
            }

            nRet = new BigDecimal(strTemp);
        } catch (Exception ex) {
        }

        return nRet;
    }

    public static boolean getBooleanValue(String strTemp) {
        boolean nRet = false;

        try {
            if (StringUtils.equalsNull(strTemp)) {
                return nRet;
            }
            nRet = Boolean.parseBoolean(strTemp);
        } catch (Exception ex) {
        }

        return nRet;
    }

    public static boolean equals(String string1, String string2) {
        boolean b = false;

        try {
            b = string1.equals(string2);
        } catch (Exception ex) {
        }

        return b;
    }

    public static boolean test(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为字母
     *
     * @param fstrData
     * @return
     */
    public static boolean isEnglishStart(String fstrData) {
        try {
            char c = fstrData.charAt(0);
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private final static int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
            Integer.MAX_VALUE};

    /**
     * 计算一个整数的大小
     *
     * @param x
     * @return
     */
    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= SIZE_TABLE[i]) {
                return i + 1;
            }
    }

    /**
     * 判断字符串的每个字符是否相等
     *
     * @param str
     * @return
     */
    public static boolean isCharEqual(String str) {
        return str.replace(str.charAt(0), ' ').trim().length() == 0;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        return (c >= 0x0391 && c <= 0xFFE5);
    }

}
