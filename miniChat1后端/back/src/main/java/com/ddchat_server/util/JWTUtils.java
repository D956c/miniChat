package com.ddchat_server.util;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JWTUtils {

    private static final String SING = "0298dsdawdx1231";
    // private static final long expireTime = Long.MAX_VALUE; // 永不过期
    private static final long expireTime = 1000 * 3600 * 24 * 5;//五天的有效期

    /**
     * 生成token
     */

    public static String getToken(Map<String, Object> map) {
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(map)
                .setSubject("token")
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .setId((String) map.get("id"))
                .signWith(SignatureAlgorithm.HS256, SING)
                .compact();
    }

    public static JSONObject parseJWT(String token) {
        JwtParser jwtParser = Jwts.parser();
        return JSONUtil.parseObj(jwtParser.setSigningKey(SING).parseClaimsJws(token).getBody());
    }

    /**
     * 验证token合法性
     */
    public static Boolean verify(String token) {
        JwtParser jwtParser = Jwts.parser();
        try {
            jwtParser.setSigningKey(SING).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
