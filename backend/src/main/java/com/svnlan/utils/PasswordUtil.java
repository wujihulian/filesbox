package com.svnlan.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 使用:
 * 直接使用即可,至于具体参数,看下面方法的说明即可;
 * 1. PasswordUtil.MD5Encode();  MD5不可逆简单加密:散列函数-基于HASH算法的编码
 * 2. PasswordUtil.AESEncrypt(); AES加密
 * 3. PasswordUtil.AESDecrypt(); AES解密
 * 4. PasswordUtil.parseByte2HexStr(); 二进制到16进制
 * 5. PasswordUtil.parseHexStr2Byte(); 16进制到2进制
 */
public class PasswordUtil {

    /**
     * 16进制的字符数组
     */
    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String passwordEncode(String source, String salt) {
        return MD5(MD5(MD5(source, salt), salt));
    }

    public static String MD5(String source) {
        return MD5Encode(source, "UTF-8", true);
    }

    public static String MD5(String source, String salt) {
        return MD5Encode(source + salt, "UTF-8", true);
    }

    public static void main(String[] args) {
        System.out.println(passwordEncode("111111", "leo"));
        System.out.println(passwordEncode("111111", "admin"));
    }

    /**
     * @param source 原字符串
     * @param encoding 指定编码类型
     * @param uppercase 是否转为大写字符串
     */
    public static String MD5Encode(String source, String encoding, boolean uppercase) {
        String result = null;
        try {
            result = source;
            // 获得MD5摘要对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组更新摘要信息
            messageDigest.update(result.getBytes(encoding));
            // messageDigest.digest()获得16位长度
            // result = parseByte2HexStr(messageDigest.digest());
            result = byteArrayToHexString(messageDigest.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uppercase ? result.toUpperCase() : result;
    }

    /**
     * AES 加密
     *
     * @param content 需要加密的内容
     * @param aesKey 加密密钥
     * @return
     */
    public static byte[] AESEncrypt(String content, String aesKey) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(aesKey.getBytes()));
            SecretKey     secretKey    = kgen.generateKey();
            byte[]        enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key          = new SecretKeySpec(enCodeFormat, "AES");
            Cipher        cipher       = Cipher.getInstance("AES");// 创建密码器
            byte[]        byteContent  = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param aesKey 解密密钥 秘miyao
     * @return
     */
    public static byte[] AESDecrypt(byte[] content, String aesKey) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(aesKey.getBytes()));
            SecretKey     secretKey    = kgen.generateKey();
            byte[]        enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key          = new SecretKeySpec(enCodeFormat, "AES");
            Cipher        cipher       = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low  = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    //byte转16进制
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    //16进制转byte[]
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

}
