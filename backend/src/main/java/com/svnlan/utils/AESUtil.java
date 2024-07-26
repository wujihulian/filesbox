package com.svnlan.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @Author:
 * @Description:
 */
public class AESUtil {
    private static final String PASSWORD = "2F99919586B40CDB40323C0DFBA12C";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    private static final String UTF8_ENCODE = "UTF-8";
    static{
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            System.out.println("security provider BC not found");
            Security.addProvider(new BouncyCastleProvider());
        }
    }


    private static final String KEY = "KNAL6GY9bVJR33M8";
    private static final String KEY_MODEL = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password, Boolean getKey2) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes(UTF8_ENCODE);

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password, getKey2));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception e) {
            LogUtil.error(e, "AES加密失败");
        }

        return null;
    }

    public static String encrypt(String content, String password){
        return encrypt(content, password, false);
    }
    public static String enctypt(String content, String password) {
        return encrypt(content, password, false);
    }

    public static String decrypt(String content, String password) {
        return decrypt(content, password, false);
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password, boolean getKey2) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password, getKey2));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, UTF8_ENCODE);
        } catch (Exception e) {
            LogUtil.error(e, "AES解密失败");
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password, boolean getKey2) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        //AES 要求密钥长度为 128
        kg.init(128, random);

        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(getKey2 ? password.getBytes() : secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥

    }

    public static String encryptionOperate(String content) {
        String key = new StringBuilder(PASSWORD).reverse().toString();
        return enctypt(new StringBuilder(enctypt(content, PASSWORD)).reverse().toString(), key);
    }

    public static String decryptionOperate(String content) {
        String key = new StringBuilder(PASSWORD).reverse().toString().toUpperCase();
        return decrypt(new StringBuilder(decrypt(content, key)).reverse().toString(), PASSWORD);
    }

    public static String aes(String data, String key, int encryptMode) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(data) || org.springframework.util.StringUtils.isEmpty(key)) {
                return null;
            }
            SecretKey secretKeySpec = new SecretKeySpec(key.getBytes(UTF8_ENCODE), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_MODEL);
            cipher.init(encryptMode, secretKeySpec);
            boolean b = encryptMode == Cipher.ENCRYPT_MODE;
            if (b) {
                byte[] doFinal = cipher.doFinal(data.getBytes(UTF8_ENCODE));
                return new String(new BASE64Encoder().encode(doFinal));
            } else {
                byte[] byte_content = new BASE64Decoder().decodeBuffer(data);
                byte[] doFinal = cipher.doFinal(byte_content);
                return new String(doFinal, UTF8_ENCODE);
            }
        } catch (Exception e) {
            LogUtil.error(e, "AES加密失败");
        }
        return null;
    }

}