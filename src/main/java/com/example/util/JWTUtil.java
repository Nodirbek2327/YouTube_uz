package com.example.util;

import com.example.dto.JwtDTO;
import com.example.exp.UnAuthorizedException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JWTUtil {

//    @Value("${jwt.secret.key}")
    private static String secretKey="youtube@123";
//    @Value("${jwt.token.life.time}")
    private static int tokenLiveTime=1000 * 3600 * 24;

    public static String encode(Integer id, String email) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);
        jwtBuilder.claim("id", id);
        jwtBuilder.claim("email", email);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.setIssuer("you_tube_uz test");
        return jwtBuilder.compact();
    }

    public static JwtDTO decode(String token) {
        try {
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(secretKey);
            Jws<Claims> jws = jwtParser.parseClaimsJws(token);
            Claims claims = jws.getBody();
            Integer id = (Integer) claims.get("id");
            String email = (String) claims.get("email");
            return new JwtDTO(id, email);
        } catch (JwtException e) {
            throw new UnAuthorizedException("Your session expired");
        }
    }

}
