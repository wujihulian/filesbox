package com.svnlan.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class DESEncrypt {

    public static String encode(String content, String key, String iv) throws Exception{
        // 设置密钥参数
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        // 设置向量
        AlgorithmParameterSpec theIV = new IvParameterSpec(iv.getBytes());
        // 获得密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key theKey = keyFactory.generateSecret(keySpec);// 得到密钥对象
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 设置工作模式为加密模式，给出密钥和向量
        enCipher.init(Cipher.ENCRYPT_MODE, theKey, theIV);
        byte[] pasByte = enCipher.doFinal(content.getBytes("utf-8"));
        return Base64.encodeBase64String(pasByte).replaceAll("\\+", "_").replaceAll("\\/", "@");

    }
}
