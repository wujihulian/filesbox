package com.svnlan.utils;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptUtil {


    public static StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("WJHL2023");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    public static void main(String[] args) {
        // 加密
        String encPwd = stringEncryptor().encrypt( "eDrkrhZ>l2sV");
        System.out.println(encPwd);
        // 解密
        String decPwd = stringEncryptor().decrypt("pwyoWlsqk/8IikLX/w4+UUhZnAMoHu5y");
        System.out.println(decPwd);
    }
}
