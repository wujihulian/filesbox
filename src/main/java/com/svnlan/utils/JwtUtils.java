package com.svnlan.utils;

import com.svnlan.common.CheckResult;
import com.svnlan.jwt.constant.SystemConstant;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/** @Description:jwt加密和解密的工具类   */
public class JwtUtils implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    /**
     * 签发JWT @Author
     *
     * @param id
     * @param subject 可以是JSON数据 尽可能少
     */
    public static String createJWT(String id, String subject, Map<String, Object> claimsMap, Long TTL) {
        return createJWT(id, subject, claimsMap, TTL, false);
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr) {
        return validateJWT(jwtStr, false);
    }

    /**
     * 密匙
     *
     * @return
     */
    public static SecretKey generalKey(Boolean isParent) {
        byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECRET);
        //byte[] encodedKey = Base64.decode(isParent ? SystemConstant.JWT_PARENT_SECRET : SystemConstant.JWT_SECRET);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @param isParent
     * @return
     */
    public static Claims parseJWT(String jwt, Boolean isParent) {
        return Jwts.parser().setSigningKey(generalKey(isParent)).parseClaimsJws(jwt).getBody();
    }

    /**
     * 生成token失效时间
     *
     * @return token失效时间
     */
    private static Date generateExpirationDate(Long TTL) {
        if (TTL == null) {
            return new Date(System.currentTimeMillis() + SystemConstant.JWT_TTL);
        }
        return new Date(System.currentTimeMillis() + TTL);
    }

    public static String createJWT(String id, String subject, Map<String, Object> claimsMap, Long TTL, Boolean isParent) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder =
                Jwts.builder()
                        .setId(id)
                        .setSubject(subject) // 主题
                        .setClaims(claimsMap)
                        .setIssuer("无极") // 签发者
                        .setIssuedAt(now) // 签发时间
                        .signWith(SignatureAlgorithm.HS256, generalKey(isParent)); // 签名算法以及密匙
        builder.setExpiration(generateExpirationDate(TTL)); // 过期时间
        return builder.compact();
    }


    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr, Boolean isParent) {
        CheckResult checkResult = new CheckResult();
        try {
            Claims claims = parseJWT(jwtStr, isParent);
            checkResult.setErrCode(0);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            LogUtil.error(e, "validateJWT ExpiredJwtException jwtStr=" + jwtStr + "， isParent=" + isParent);
            checkResult.setErrCode(SystemConstant.JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            LogUtil.error(e, "validateJWT SignatureException jwtStr=" + jwtStr + "， isParent=" + isParent);
            checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            LogUtil.error(e, "validateJWT Exception jwtStr=" + jwtStr + "， isParent=" + isParent);
            checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }
}
