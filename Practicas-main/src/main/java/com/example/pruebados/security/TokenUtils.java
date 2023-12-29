package com.example.pruebados.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private final static String ACCESS_TOKEN_SECRET = "s9HkG8rNjBpE4LmTqVwX7cF2uY3ZaD6fA5xW1iU2y";

  //  private final static String ACCESS_TOKEN_SECRET = "token1234";
    //SON 30 DIAS DE CADUCIDAD, 30 DIAS EN SEGUNDOS. 2 MILLONES
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS= 2_592_000L;


    public static String createToken(String nombre, String email){

        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS *1_000;
        Date expirationDate = new Date (System.currentTimeMillis()+expirationTime);

        Map<String,Object> extra  = new HashMap<>();
        extra.put("nombre", nombre);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

    }

    public static UsernamePasswordAuthenticationToken getAuthetication (String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()

                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }catch (JwtException e){
            System.out.println(e.getMessage());
            return  null;
        }
    }
}
