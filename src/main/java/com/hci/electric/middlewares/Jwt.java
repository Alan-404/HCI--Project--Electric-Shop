package com.hci.electric.middlewares;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Jwt {
    private String secretKey = "alanShop04042001";

    public String extractToken(HttpServletRequest httpServletRequest){
        try{
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            if (authorizationHeader == null || authorizationHeader.startsWith("Bearer") == false){
                return null;
            }
            String token = authorizationHeader.split(" ")[1];
            return token;
        }
        catch(Exception exception){
            return null;
        }
    }

    public String authorize(HttpServletRequest httpServletRequest){
        try{
            String token = this.extractToken(httpServletRequest);
            if (token == null){
                return null;
            }

            String userId = this.extractUserId(token);
            if (userId == null){
                return null;
            }
    
            return userId;
        }
        catch(Exception exception){
            return null;
        }
    }

    public String extractUserId(String token){
        try{
            return extractClaim(token, Claims::getSubject);
        }   
        catch(Error err){
            return null;
        }
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String generateToken(String accountId) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, accountId);
    }
    
    private String buildToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
}
