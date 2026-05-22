package com.dealer.drproj.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY; 
    private final long TTL=1000*60*60*2;
    
    private SecretKey getSignngKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String msisdn, String role){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role","ROLE_"+role);
        return Jwts.builder().claims(claims).subject(msisdn).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis()+TTL)).signWith(getSignngKey()).compact();
    }

    public String extractMsisdn(String token){return extractAllClaims(token).getSubject();}
    public String extractRole(String token){return extractAllClaims(token).get("role",String.class);}
    public boolean isTokenValid(String token,String msisdn){return extractMsisdn(token).equals(msisdn) && !isTokenExpired(token);}
    public boolean isTokenExpired(String token){return extractAllClaims(token).getExpiration().before(new Date());}

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSignngKey()).build().parseSignedClaims(token).getPayload();
    }
}
