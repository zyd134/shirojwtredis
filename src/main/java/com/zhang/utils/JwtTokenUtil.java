package com.zhang.utils;

import com.zhang.config.TokenSetting;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
public class JwtTokenUtil {
    /**
     * # JWT 密钥
     * # 业务短时间刷新Token过期时间
     * # PC端长时间刷新Token自动刷新时间
     * # APP端长时间刷新Token自动刷新时间
     * # 发行人
     * 我们也可以不将以上数据写在配置文件中，可以直接写在此类中作为此类的成员静态常量，或者独立一个公共静态常量类，另外一种jwt写法详见工程
     */
    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String issuer;

    // 初始化JWT的方法 // 取得JWT配置类中取得的配置文件中的默认值
    public static void setJwtProperties(TokenSetting tokenSetting) {
        secretKey = tokenSetting.getSecretKey();
        accessTokenExpireTime = tokenSetting.getAccessTokenExpireTime();
        refreshTokenExpireTime = tokenSetting.getRefreshTokenExpireTime();
        refreshTokenExpireAppTime = tokenSetting.getRefreshTokenExpireAppTime();
        issuer = tokenSetting.getIssuer();
    }


    /**
     * 生成短时间刷新access_token
     * @param subject 用户ID
     * @param claims 载荷部分参数，即此用户的个人信息
     * @return
     */
    public static String getAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, accessTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * 此方法需要详细解读
     * 签发token
     * @param issuer    签发人
     * @param subject   代表这个JWT的主体，即它的所有人 一般是用户id
     * @param claims    存储在JWT里面的信息 一般放些用户的权限/角色信息
     * @param ttlMillis 有效时间(毫秒)
     */
    public static String generateToken(String issuer, String subject, Map<String, Object> claims, long ttlMillis, String secret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);
        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam("type", "JWT");
        if (null != claims) {
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }


    // 配置签发长时间刷新token的静态方法
    // 上面我们已经有生成 access_token 的方法，下面加入生成 refresh_token 的方法(PC 端过期时间短一些)
    /**
     * 生产PC端长时间刷新refresh_token
     */
    public static String getRefreshToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireTime.toMillis(),secretKey);
    }


    //上面我们已经有生成 access_token 的方法，下面加入生成 refresh_token 的方法(APP 端过期时间长一些)
    /**
     * 生产App端长时间刷新refresh_token
     */
    public static String getRefreshAppToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireAppTime.toMillis(),secretKey);
    }

    /**
     * 从令牌负载中获取数据
     * 解析token获取数据的静态方法
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            if(e instanceof ClaimJwtException){
                claims=((ClaimJwtException) e).getClaims();
            }
        }
        return claims;
    }

    /**
     * 获取用户id
     * 通过token 获取userId 静态方法
     */
    public static String getUserId(String token){
        String userId=null;
        try {
            // 取得负载内容
            Claims claims = getClaimsFromToken(token);
            // 从负载内容中取得UserID
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return userId;
    }

    /**
     * 获取用户名
     */
    public static String getUserName(String token){
        String username=null;
        try {
            // 取得负载内容
            Claims claims = getClaimsFromToken(token);
            // 使用用户名对应的key名，取得负责内容中的用户名值
            username = (String) claims.get(Constant.JWT_USER_NAME);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return username;
    }

    /**
     *  token 是否过期方法
     * 校验令牌(true：未过期 false：已经过期)
     * @param token
     * @return
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            // Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false
            // 当expiration大于new Date()时，即过期时间大于当前时间，返回false
            boolean before = expiration.before(new Date()); // 显然此处返回false
            return before;
        } catch (Exception e) {
            log.error("error={}",e);
//            return false;
            return true; // 此处为true的原因不明
        }
    }

    /**
     * 验证token是否有效的方法
     * 校验令牌(true：验证通过 false：验证失败)
     * @param token
     * @return
     */
    public static Boolean validateToken(String token) {
        // 取得Token解析出来的数据
        Claims claimsFromToken = getClaimsFromToken(token);
        //果解析出来的数据是否为空 且 Token是否已经过期
        // !isTokenExpired(token)意思是如果token在有效期择返回false的取反true
        return (null!=claimsFromToken && !isTokenExpired(token));
    }

    /**
     * 获取token的剩余过期时间方法
     * 获取token的剩余过期时间
     * @param token
     * @return
     */
    public static long getRemainingTime(String token){
        long result=0;
        try {
            long nowMillis = System.currentTimeMillis();
            result= getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return result;
    }
}
