package util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secret;

    public String genJsonWebToken(long id){
        System.out.println(secret);
        Map<String ,Object> headers = new HashMap<String, Object>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        Map<String, Object> payloads = new HashMap<String, Object>();
        payloads.put("id", id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date exp = calendar.getTime();
        return Jwts.builder().setHeader(headers).setClaims(payloads).setExpiration(exp).signWith(SignatureAlgorithm.HS256, secret.getBytes()).compact();
    }

    public boolean isValid(String token) throws Exception{
        if(token == null) throw new Exception("토큰이 없습니다.");
        if(!token.startsWith("Bearer ")) throw new Exception("올바른 토큰이 아닙니다.");
        token = token.substring(7);
        try{
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e){
            throw new Exception("토큰이 잘못되었습니다");
        }
        return true;
    }

    public Long getIdFromJWT(String token){
        Claims claims = Jwts
                .parser().setSigningKey(secret.getBytes())
                .parseClaimsJws(token.substring(7)).getBody();
        return Long.parseLong(String.valueOf(claims.get("id")));
    }
}
