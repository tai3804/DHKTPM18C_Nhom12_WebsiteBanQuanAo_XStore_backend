package iuh.fit.xstore.util;

import io.jsonwebtoken.Jwts;
import iuh.fit.xstore.config.JwtConfig;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 * Copyright (c) 2025 by tai
 * All rights reserved.
 *
 * Created: 10/15/2025
 */

@Component
public class jwtUtil {

    //tạo token để gửi về cho fe
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)              // thông tin user
                .setIssuedAt(new Date())           // thời gian tạo
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfig.EXPIRATION_TIME)) // hết hạn
                .signWith(JwtConfig.SECRET_KEY)   // ký token
                .compact();
    }
}
